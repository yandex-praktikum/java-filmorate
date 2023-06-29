package ru.yandex.practicum.filmorete.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Film {

    private Integer id;

    @NonNull
    @NotBlank
    private final String name;

    @NonNull
    @NotBlank
    private final String description;

    @NonNull
    private final LocalDate releaseDate;

    @NonNull
    private final Integer duration;
}