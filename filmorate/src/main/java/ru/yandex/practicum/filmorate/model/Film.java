package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
//    @NonNull
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
