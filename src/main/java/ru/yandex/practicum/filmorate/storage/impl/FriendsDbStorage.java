package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.List;


@Component
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * статусы дружбы: если ты добавляешь друга, который уже добавил тебя, оба ваши статуса становятся confirm,
     * иначе request
     */
    @Override
    public boolean addFriend(Long userId, Long friendId) {
        if (!isUserExist(userId) || !isUserExist(friendId))
            throw new UserNotFoundException();

        String sqlForCheck = "select friendship_status from friends where user_id = ? and friend_id = ?";

        if (jdbcTemplate.queryForRowSet(sqlForCheck, userId, friendId).next())
            throw new AlreadyFriendsException();

        String sql = "insert into friends (user_id, friend_id, friendship_status) values (?, ?, ?)";

        if (jdbcTemplate.queryForRowSet(sqlForCheck, friendId, userId).next()) {
            jdbcTemplate.update(sql, userId, friendId, "confirm");
            jdbcTemplate.update("UPDATE friends SET friendship_status = 'confirm' " +
                            "WHERE user_id = ? and friend_id = ?",
                    friendId, userId);
        } else {
            jdbcTemplate.update(sql, userId, friendId, "request");
        }

        return true;
    }

    @Override
    public List<User> getFriends(Long userId) {
        if (!isUserExist(userId))
            throw new FriendNotFoundException();

        String sql = "select  u.user_id, email, login, name, birthday " +
                "from friends as f " +
                "join users as u on f.friend_id = u.user_id " +
                "where f.user_id = ?";

// тут теперь всегда вывадает одно и то же, параметр ? = 1 должн быть

        return jdbcTemplate.query(sql, (rs, rowNum) -> UserDbStorage.makeUser(rs), userId);
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        if (!isUserExist(userId) || !isUserExist(friendId))
            throw new UserNotFoundException();


        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select friendship_status from friends " +
                "where user_id = ? and friend_id = ?", userId, friendId);
        rowSet.next();
        if (rowSet.getString("friendship_status").equals("confirm"))
            jdbcTemplate.update("UPDATE friends SET friendship_status = 'request' " +
                    "WHERE user_id = ? and friend_id = ?", friendId, userId);

        String sql = "DELETE FROM friends WHERE user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);

        return true;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        if (!isUserExist(userId) || !isUserExist(otherId))
            throw new UserNotFoundException();

        String sql = "select  u.user_id, u.email, u.login, u.name, u.birthday " +
                "from friends as f " +
                "join users as u on f.friend_id = u.user_id " +
                "where f.user_id = ? and f.friend_id IN (" +
                "select friend_id from friends where user_id = ?)";

        return jdbcTemplate.query(sql, (rs, rowNum) -> UserDbStorage.makeUser(rs), userId, otherId);
    }

    /**
     * Проверка, существует ли пользователь с таким id
     *
     * @param id
     * @return
     */
    private boolean isUserExist(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select user_id from users where user_id = ?", id);
        return userRows.next();
    }
}
