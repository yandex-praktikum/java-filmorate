package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.NotReleaseDateBefore1895;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @NotReleaseDateBefore1895
    private String releaseDate;
    @Min(1)
    private long duration;
}