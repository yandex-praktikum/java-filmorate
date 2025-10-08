package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.ecxeption.DatabaseException;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.ecxeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE_QUERY = """
            INSERT INTO users (email,login,name,birthday) VALUES (?,?,?,?)
            """;
    private static final String UPDATE_QUERY = """
            UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?
            """;
    private static final String GET_ID_QUERY = """
            SELECT * FROM users WHERE id = ?
            """;
    private static final String GET_ALL_QUERY = """
            SELECT * FROM users
            """;
    private static final String CREATE_FRIENDSHIP_QUERY = """
            INSERT INTO friends (senderUser_id, receiverUser_id, status) VALUES (?,?,?)
            """;
    private static final String FIND_RECEIVER_FRIENDSHIP_QUERY = """
            SELECT senderUser_id FROM friends WHERE receiverUser_id = ?
            """;
    private static final String FIND_SENDER_FRIENDSHIP_QUERY = """
            SELECT receiverUser_id FROM friends WHERE senderUser_id = ?
            """;
    private static final String UPDATE_FRIENDSHIP_QUERY = """
            UPDATE friends SET status = ? WHERE senderUser_id =? AND receiverUser_id = ?
            """;
    private static final String NOT_CONFIRMED_FRIENDSHIP_QUERY = """
            SELECT senderUser_id FROM friends WHERE receiverUser_id = ?
            """;
    private static final String CONFIRMED_FRIENDSHIP_QUERY = """
            SELECT receiverUser_id FROM friends WHERE senderUser_id = ? AND status = true
            """;
    private static final String DELETE_FRIENDSHIP_QUERY = """
            DELETE FROM friends WHERE (senderUser_id = ? AND receiverUser_id = ?) OR
            (receiverUser_id = ? AND senderUser_id = ?)
            """;

    @Override
    public User getUserById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_ID_QUERY, new UserRowMapper(jdbcTemplate), id);
        } catch (DataAccessException e) {
            throw new DatabaseException("Такого юзера нет в списке!");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(GET_ALL_QUERY, new UserRowMapper(jdbcTemplate));
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан!");
        }
        if (getAllUsers().stream().map(User::getId).collect(Collectors.toSet()).contains(user.getId())) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(UPDATE_QUERY);
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                stmt.setLong(5, user.getId());
                return stmt;
            });
            return user;
        } else throw new NotFoundException("Такого пользователя нет в списке!");
    }

    public User createFriendship(long receiverUserId, long senderUserId) {
        List<Long> res = jdbcTemplate.queryForList(FIND_RECEIVER_FRIENDSHIP_QUERY, Long.class, senderUserId);
        List<Long> rest = jdbcTemplate.queryForList(FIND_SENDER_FRIENDSHIP_QUERY, Long.class, receiverUserId);
        if (res.contains(receiverUserId)) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(UPDATE_FRIENDSHIP_QUERY);
                stmt.setBoolean(1, true);
                stmt.setLong(2, receiverUserId);
                stmt.setLong(3, senderUserId);
                return stmt;
            });
            return getUserById(senderUserId);
        } else if (!res.contains(receiverUserId) && !rest.contains(senderUserId)) {

            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(CREATE_FRIENDSHIP_QUERY);
                stmt.setLong(1, senderUserId);
                stmt.setLong(2, receiverUserId);
                stmt.setBoolean(3, false);
                return stmt;
            });
            return getUserById(senderUserId);
        } else {
            return getUserById(senderUserId);
        }
    }

    public Collection<User> listOfFriends(long id) {
        List<Long> notConfirmedFriends = jdbcTemplate.queryForList(NOT_CONFIRMED_FRIENDSHIP_QUERY, Long.class, id);
        List<Long> confirmedFriends = jdbcTemplate.queryForList(CONFIRMED_FRIENDSHIP_QUERY, Long.class, id);
        Set<Long> friendsIds = Stream.concat(notConfirmedFriends.stream(), confirmedFriends.stream()).collect(Collectors.toSet());
        return friendsIds.stream()
                .map(ids -> getUserById(ids))
                .collect(Collectors.toSet());
    }

    public Collection<User> listOfCommonFriends(Long id, Long otherId) {
        Collection<User> commonFriends = listOfFriends(id);
        commonFriends.retainAll(listOfFriends(otherId));
        return commonFriends;
    }

    public User deleteFriendship(long id, long friendId) {
        List<Long> notConfirmedFriends = jdbcTemplate.queryForList(NOT_CONFIRMED_FRIENDSHIP_QUERY, Long.class, id);
        List<Long> confirmedFriends = jdbcTemplate.queryForList(CONFIRMED_FRIENDSHIP_QUERY, Long.class, id);
        if (confirmedFriends.contains(friendId)) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(UPDATE_FRIENDSHIP_QUERY);
                stmt.setBoolean(1, false);
                stmt.setLong(2, id);
                stmt.setLong(3, friendId);
                return stmt;
            });
        } else if (notConfirmedFriends.contains(friendId)) {
            jdbcTemplate.update(DELETE_FRIENDSHIP_QUERY, id, friendId, id, friendId);
        }
        return getUserById(id);
    }
}
