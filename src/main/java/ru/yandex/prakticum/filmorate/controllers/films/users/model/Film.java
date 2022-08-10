package ru.yandex.prakticum.filmorate.controllers.films.users.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.sql.Time;
import java.time.LocalDate;
@Data
@Builder
public class Film {
    @NonNull
    private final Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Time duration;
}
