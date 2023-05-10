package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
//@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
public class Film {


    int id;
    //    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    int rate;

}
