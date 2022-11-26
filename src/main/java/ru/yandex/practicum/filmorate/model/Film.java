package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.time.Duration;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Film {
    private int id;//целочисленный идентификатор — id;
    private String name;//название — name;
    private String description;//описание — description;
    private LocalDate releaseDate;//дата релиза — releaseDate;
    private int duration;//продолжительность фильма — duration.

}
