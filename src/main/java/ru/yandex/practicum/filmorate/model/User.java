package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
// Класс User
public class User {

    // Уникальный идентификатор пользователя
    private int id;
    // Электронная почта пользователя
    private String email;
    // Логин пользователя
    private String login;
    // Имя пользователя
    private String name;
    // Дата рождения пользователя
    private LocalDate birthday;
}
