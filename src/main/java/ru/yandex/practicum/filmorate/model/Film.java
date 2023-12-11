package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Valid
public class Film {

    private Integer id;
    @NotBlank(message = "{name.film.not_blank}")
    private String name;

    @Size(max = 200, message = "{description.film.size}")
    @NotBlank(message = "{description.film.not_blank}")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "{release.film.not_future}")
    private LocalDate releaseDate;

    @Positive(message = "{duration.film.positive}")
    private Float duration;

}
