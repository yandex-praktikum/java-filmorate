package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.ValidDate;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString

public class Film {


    int id ;
    @NotBlank
    String name;
    @Size(min = 1, max = 200, message = "{максимальная длина описания — 200 символов}")
    String description;
     @ValidDate
    LocalDate releaseDate;
     @Positive
    int duration;
    int rate;


}
