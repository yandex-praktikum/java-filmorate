package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmReleaseDate;

import java.time.LocalDate;


@Data
public class Film {

    private int id;
    @NotBlank(message = "Film name can't be blank")
    private String name;
    @Size(max = 200, message = "Film description has to be less than 200 symbols")
    @NotBlank(message = "Film description can't be blank")
    private String description;
    @FilmReleaseDate(message = "The release date has to be before today")
    private LocalDate releaseDate;
    @NotNull(message = "The film duration can't be empty")
    @Positive(message = "The film duration can't be positive")
    private Integer duration;

    public Film(int id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
