package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    private int id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;

}
