package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;

import java.time.LocalDate;

@Data
public class Film {
    private final long id = FilmIdGenerator.generate();
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final short duration;
}
