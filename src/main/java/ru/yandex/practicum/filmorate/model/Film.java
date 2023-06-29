package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {
    @PositiveOrZero
    private int id;
    @NotNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
