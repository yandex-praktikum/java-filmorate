package ru.yandex.practicum.filmorate.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Relationship {
    public Relationship(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    private int userId;
    private int friendId;

    public Map<String, Object> toMap(){
        Map<String, Object> values = new HashMap<>();
        values.put("USER_ID", userId);
        values.put("FRIEND_ID", friendId);
        return values;
    }
}
