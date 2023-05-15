package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private long id; // идентификатор
    @NotBlank
    private String name; // название
    @Size(max = 200)
    private String description; // описание
    @PastOrPresent
    private LocalDate releaseDate; // дата релиза
    @PositiveOrZero
    private int duration; // продолжительность фильма
}