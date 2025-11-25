package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import ru.yandex.practicum.filmorate.validation.AfterInclude;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @PastOrPresent
    @AfterInclude("1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private MpaRating mpa;

    @Singular("genre")
    private Set<Genre> genres = new LinkedHashSet<>();

    @Builder.Default
    private Set<Long> likes = new HashSet<>();
}
