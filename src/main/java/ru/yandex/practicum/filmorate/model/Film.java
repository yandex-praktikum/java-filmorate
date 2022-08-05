package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    @Min(1)
    private final int id;
    @NotBlank
    private final String name;
    @Size(min = 0,max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Min(1)
    private final Integer duration;
}
