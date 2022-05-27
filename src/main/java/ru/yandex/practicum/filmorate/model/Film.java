package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private final long id = FilmIdGenerator.generate();
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @NotBlank
    private final String description;
    @NotNull
    @Past
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final short duration;
}
