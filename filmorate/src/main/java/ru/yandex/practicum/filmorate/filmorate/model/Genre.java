package ru.yandex.practicum.filmorate.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> values = new HashMap<>();
        values.put("GENRE_NAME", name);
        return values;
    }
}
