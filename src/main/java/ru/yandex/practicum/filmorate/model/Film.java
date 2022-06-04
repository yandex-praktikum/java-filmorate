package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {
    public static long countFilm;
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
