package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select  user_id, email, login, name, birthday from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    /**
     * Маппер
     */
    protected static User makeUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    @Override
    public User getUser(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);

        if(userRows.next()) {
            User user = new User(
                userRows.getLong("user_id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("name"),
                userRows.getDate("birthday").toLocalDate()
            );

            return user;
        } else {
            return null;
        }
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Long id = simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue();

        return getUser(id);
    }

    /** Для работы метода SimpleJdbcInsert.executeAndReturnKey
     * @param user
     * @return
     */
    protected static Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }



    @Override
    public User update(User user) {
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getUser(user.getId());
    }
}
