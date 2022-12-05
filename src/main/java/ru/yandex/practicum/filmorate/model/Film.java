package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Long rate;
    private final Set<Long> usersLike = new HashSet<>();



    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Long rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
    public void addLike(Long id) {
        usersLike.add(id);
    }

    public void removeLike(Long id) {
        usersLike.remove(id);
    }

    public Set<Long> getLikes() {
        return usersLike;
    }
}
