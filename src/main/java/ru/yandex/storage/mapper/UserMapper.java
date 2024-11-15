package ru.yandex.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String login = rs.getString("login");
        LocalDate birthday = LocalDate.parse(rs.getString("birthday")); // ВАЖНЫЙ МОМЕНТ!!!
        return User.builder().id(id).email(email).login(login).birthday(birthday).build();
    }
}
