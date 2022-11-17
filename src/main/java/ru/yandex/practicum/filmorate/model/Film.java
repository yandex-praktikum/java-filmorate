package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class Film {
    int id;
    Optional<String> name;
    String description;
    LocalDate releaseDate;
    long duration; //минуты
}
