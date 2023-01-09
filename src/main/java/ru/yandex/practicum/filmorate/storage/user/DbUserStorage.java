package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.IncorrectParameterException;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private Long userId = 0L;
    private static final String SQL_INS_USERS =
            "INSERT INTO users(id_user,name,login,email,birthday) values(?,?,?,?,?)";
    private static final String SQL_UPD_USERS = "UPDATE users SET name=?,login=?,email=?,birthday=? WHERE id_user=?";
    private static final String SQL_DEL_USERS = "DELETE FROM users WHERE id_user=?";
    private static final String SQL_DEL_USERS_ALL = "DELETE FROM users";
    private static final String SQL_INS_FRIENDS = "INSERT INTO friends(id_user,id_friend,status) values(?,?,1)";
    private static final String SQL_DEL_FRIENDS = "DELETE FROM friends WHERE id_user=? and id_friend=?";

    private Long getUserId() {
        return ++userId;
    }

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getById(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("invalid id");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<User> createUser(User user) {

        if (user.getName().equals("") || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            throw new ValidateException("неправильный формат email или пустой email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidateException("пустой логин или содержит пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("дата рождения указывает на будущее время");
        }


        Long id = getUserId();
        jdbcTemplate.update(SQL_INS_USERS, id, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT U.ID_USER,F.ID_FRIEND FROM USERS U "
                        + "LEFT JOIN FRIENDS F on u.ID_USER = F.ID_FRIEND "
                        + "WHERE U.ID_USER=?"
                , id);
        while (userRow.next()) {
            user.setId(userRow.getLong("id_user"));
            user.addFriend(userRow.getLong("id_friend"));
        }

        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (user.getId() <= 0 || user.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        jdbcTemplate.update(SQL_UPD_USERS, user.getName(), user.getLogin(), user.getEmail()
                , user.getBirthday(), user.getId());
        return Optional.of(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        while (userRows.next()) {
            User user = new User(
                    userRows.getLong("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update(SQL_DEL_USERS, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(SQL_DEL_USERS_ALL);
    }

    @Override
    public void addFriend(Long id, Long idFriend) {
        jdbcTemplate.update(SQL_INS_FRIENDS, id, idFriend);
    }

    @Override
    public void deleteFriend(Long id, Long idFriend) {
        jdbcTemplate.update(SQL_DEL_FRIENDS, id, idFriend);
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT u.* FROM friends f  "
                + "JOIN users u on f.id_friend=u.id_user "
                + "WHERE f.status=1 and f.id_user=?", id);

        while (userRows.next()) {
            User user = new User(
                    userRows.getLong("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long id2) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user IN "
                + "(SELECT id_friend FROM friends WHERE id_user=? AND id_friend IN "
                + "(SELECT id_friend FROM friends WHERE id_user=?))", id, id2);

        while (userRows.next()) {
            User user = new User(
                    userRows.getLong("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }
}
