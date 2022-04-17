package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

public class FilmUserTestData {

    public static Film film1 = new Film(1, "New film", "Some description", LocalDate.of(2020, 10, 13), Duration.ofMinutes(120));
    public static User user1 = new User(1, "dfg@mail.ru", "login", "name", LocalDate.of(1980, 5, 13));
}
