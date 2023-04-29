package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.*;

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