package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
// Класс Film
public class Film {

    // Уникальный идентификатор фильма
    private int id;
    // Название фильма
    private String name;
    // Описание фильма
    private String description;
    // Дата выхода фильма
    private LocalDate releaseDate;
    // Продолжительность фильма в минутах
    private int duration;
}
