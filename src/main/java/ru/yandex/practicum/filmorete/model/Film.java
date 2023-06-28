package ru.yandex.practicum.filmorete.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {

    private Integer id;

    private final String name;

    private final String description;

    private final LocalDate releaseDate;

    private final Integer duration;
}