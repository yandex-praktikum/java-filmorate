package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.time.LocalDate;
@Data
public class User {
    private final long id = UserIdGenerator.generate();
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;

}
