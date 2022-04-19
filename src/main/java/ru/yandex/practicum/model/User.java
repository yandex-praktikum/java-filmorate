package ru.yandex.practicum.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}