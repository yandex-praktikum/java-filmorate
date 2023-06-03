package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
