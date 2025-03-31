package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String name;
    private String login;
    private String email;
    private LocalDate birthday;
}