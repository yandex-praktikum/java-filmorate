package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class MpaRating {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
}