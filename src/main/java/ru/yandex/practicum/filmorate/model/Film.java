package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@Valid
@AllArgsConstructor
public class Film extends StorageData {

    @NotBlank(message = "{name.film.not_blank}")
    private String name;

    @Size(max = 200, message = "{description.film.size}")
    @NotBlank(message = "{description.film.not_blank}")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "{release.film.not_future}")
    private LocalDate releaseDate;

    @Positive(message = "{duration.film.positive}")
    private Long duration;

}
