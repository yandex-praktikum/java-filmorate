package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.controller.validation.FilmValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    int id;
    @Size(max = 200)
    String description;
    @FilmValid
    LocalDate releaseDate;
    @Min(1)
    long duration; //минуты
    @NonNull
    String name;

    public Film(int id, String description,LocalDate releaseDate, long duration, @NonNull String name) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.name = name;
    }
}
