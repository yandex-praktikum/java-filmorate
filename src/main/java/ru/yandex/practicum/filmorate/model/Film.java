package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private static int nextId = 1;

    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Positive
    private final long duration;

    public void setId() {
        if (id == 0) {
            id = nextId;
            nextId++;
        }
    }
}
