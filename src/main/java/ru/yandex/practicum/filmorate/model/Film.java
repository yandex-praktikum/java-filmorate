package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Film {
    final private Long id;
    @NotBlank
    final private String name;
    @NotBlank
    @Size(min = 1, max = 200)
    final private String description;
    @PastOrPresent
    final private LocalDate releaseDate;
    @Positive
    final private Integer duration;
    @NotNull
    final private RatingMPA mpa;
    final private Set<Genre> genres;
}
