package ru.yandex.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.model.User;
import ru.yandex.storage.mapper.UserMapper;

import java.sql.*;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->{
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO my_users(name, email, login, birthday) VALUES(?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getEmail());
            ps.setObject(3, user.getLogin());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long generatedId = keyHolder.getKeyAs(Long.class);
        user.setId(generatedId);

        String sqlQuery = "insert into friends(id_user, id_friend, id_status) " +
                "values (?, ?, ?);";

        for (Long el: user.getFriends()) {
            jdbcTemplate.update(sqlQuery, user.getId(), el, user.getStatus());
        }
        return user;
    }

    @Override
    public Collection<User> allUsers() {
        return jdbcTemplate.query("SELECT * FROM my_users;", userMapper);
    }

    @Override
    public User updateUser(User newUser) {
        String sqlQuery = "update my_users set" +
                "name = ?, email = ?, login = ?, birthday = ? " +
                "where id = ?;";
        jdbcTemplate.update(sqlQuery, newUser.getName(), newUser.getEmail(), newUser.getLogin(),
                newUser.getBirthday(), newUser.getId());

        jdbcTemplate.update("delete from friends where id = ?;", newUser.getId());
        for (Long el: newUser.getFriends()) {
            jdbcTemplate.update("insert into friends(id_user, id_friend, id_status) " +
                            "values (?, ?, ?);",
                    newUser.getId(), el, newUser.getStatus());
        }
        return newUser;
    }

    @Override
    public User deleteUser(User user) {
        String sqlQuery = "delete from ? where id = ?;";
        jdbcTemplate.update(sqlQuery, "my_users", user.getId());
        jdbcTemplate.update(sqlQuery, "friends", user.getId());
        return user;
    }
}
