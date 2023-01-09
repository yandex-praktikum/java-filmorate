package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre implements Comparable<Genre> {
    private Long id;
    private String name;


    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public int compareTo(Genre o) {
        return (int) (this.getId() - o.getId());
    }
}
