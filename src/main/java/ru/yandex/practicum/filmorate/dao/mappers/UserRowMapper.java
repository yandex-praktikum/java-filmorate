package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class UserRowMapper implements RowMapper<User> {
    private final JdbcTemplate jdbcTemplate;

    private static final String NOT_CONFIRMED_FRIENDSHIP_QUERY = """
             SELECT senderUser_id FROM friends WHERE receiverUser_id = ?
            """;
    private static final String CONFIRMED_FRIENDSHIP_QUERY = """
             SELECT receiverUser_id FROM friends WHERE senderUser_id = ? AND status = true
            """;

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());

        List<Long> notConfirmedFriends = jdbcTemplate.queryForList(NOT_CONFIRMED_FRIENDSHIP_QUERY, Long.class, user.getId());
        List<Long> confirmedFriends = jdbcTemplate.queryForList(CONFIRMED_FRIENDSHIP_QUERY, Long.class, user.getId());

        Set<Long> allFriends = Stream.concat(notConfirmedFriends.stream(), confirmedFriends.stream()).collect(Collectors.toSet());
        user.setFriends(allFriends);
        return user;
    }
}