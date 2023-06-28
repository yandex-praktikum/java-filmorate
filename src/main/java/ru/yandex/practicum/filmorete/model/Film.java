package ru.yandex.practicum.filmorete.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private final Integer id;

    @NonNull
    private final String name;

    @NonNull
    private final String description;

    @NonNull
    private final LocalDate releaseDate;

    @NonNull
    private final Duration duration;
}