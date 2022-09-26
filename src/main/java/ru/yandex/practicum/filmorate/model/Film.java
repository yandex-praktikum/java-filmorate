package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    @NotEmpty
    @NotBlank
    String name;
    Long id;
    String description;
    LocalDate releaseDate;
    @Positive
    long duration;

    public Film(String name, Long id, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
