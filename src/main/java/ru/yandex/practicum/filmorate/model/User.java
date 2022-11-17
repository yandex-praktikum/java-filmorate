package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class User {
    int id;
    Optional<String> email;
    Optional<String> login;
    Optional<String> name;
    LocalDate birthday;
}
