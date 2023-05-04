package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    @Min(value = 1)
    int duration;
}
