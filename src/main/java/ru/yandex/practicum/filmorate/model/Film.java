package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.boot.convert.DurationFormat;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Integer duration;


}
