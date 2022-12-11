package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.DateValidationInterface;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @PastOrPresent
    @DateValidationInterface
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
