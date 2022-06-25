package ru.yandex.practicum.filmorate.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.filmorate.model.User;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (checkUserId(userId) && checkUserId(friendId)) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("RELATIONSHIP")
                    .usingGeneratedKeyColumns("RELATIONSHIP_ID");
            Map<String, Object> friend = new HashMap<>();
            friend.put("USER_ID", userId);
            friend.put("FRIEND_ID", friendId);
            friend.put("STATUS_ID", 1);
            simpleJdbcInsert.execute(friend);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или с id \"%s\" не существует."
                            , userId, friendId));
        }
        if(isFriends(userId, friendId)){
            String sql = "update RELATIONSHIP set STATUS_ID = 2 where USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sql, userId, friendId);
            jdbcTemplate.update(sql, friendId, userId);
        }
    }
    private boolean isFriends(Integer userId, Integer friendId){
        String userFriendSql = "SELECT * FROM RELATIONSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet userFriendRow = jdbcTemplate.queryForRowSet(userFriendSql, userId, friendId);
        SqlRowSet friendUserRow = jdbcTemplate.queryForRowSet(userFriendSql, friendId, userId);
        return userFriendRow.next() && friendUserRow.next();
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (checkUserId(userId) && checkUserId(friendId)) {
            String deleteSql = "DELETE FROM RELATIONSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(deleteSql, userId, friendId);
            String updateSql = "update RELATIONSHIP set STATUS_ID = 1 where FRIEND_ID = ? AND USER_ID = ?";
            jdbcTemplate.update(updateSql, friendId, userId);
            } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или с id \"%s\" не существует."
                            , userId, friendId));
        }
    }

    public List<User> getFriendsList(Integer userId) {
        List<User> list = new ArrayList<>();
        if (checkUserId(userId)) {
            String sql = "SELECT FRIEND_ID FROM RELATIONSHIP WHERE USER_ID = ?";
            SqlRowSet friends = jdbcTemplate.queryForRowSet(sql, userId);
             while (friends.next()){
                 list.add(userStorage.getUserById(friends.getInt("FRIEND_ID")));
              }
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\"не существует.", userId));
        }
        return list;
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        List<User> list = new ArrayList<>();
        if (checkUserId(userId) && checkUserId(friendId)) {
            String sql = "SELECT FRIEND_ID FROM RELATIONSHIP WHERE USER_ID=?\n" +
                    "INTERSECT\n" +
                    "SELECT FRIEND_ID FROM RELATIONSHIP WHERE USER_ID=?";
            SqlRowSet friends = jdbcTemplate.queryForRowSet(sql, userId, friendId);
                while (friends.next()) {
                    list.add(userStorage.getUserById(friends.getInt("FRIEND_ID")));
                }
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или с id \"%s\" не существует."
                            , userId, friendId));
        }
        return list;
    }

    private boolean checkUserId(int userId){
        String sql = "SELECT * " +
                "FROM USER " +
                "WHERE USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, userId);
        return userRows.next();
    }

}