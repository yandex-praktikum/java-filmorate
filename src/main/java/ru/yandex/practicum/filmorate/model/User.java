package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
@ToString
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}