package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private long id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    public LocalDate releaseDate;

    @Min(1)
    private long duration;
}