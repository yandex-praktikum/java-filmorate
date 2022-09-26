package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotEmpty
    @NotBlank
    String login;
    String name;
    Long id;
    @Email
    String email;
    @Past
    LocalDate birthday;

    public User(String login, String name, Long id, String email, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }

}
