package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
public class Genre {
    @NotNull
    @Size(min = 1, max = 6)
    final private Long id;
    private String name;

    @JsonCreator
    public Genre(Long id) {
        this.id = id;
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
