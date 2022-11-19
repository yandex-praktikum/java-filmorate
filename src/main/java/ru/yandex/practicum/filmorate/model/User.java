package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.controller.validation.UserValid;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Optional;

@Data
public class User {
    int id;
   Optional<String> name;
    LocalDate birthday;
    @NonNull @Email
    String email;
    @NonNull
    String login;

    public User(int id, Optional<String> name, @UserValid LocalDate birthday, @NonNull String email, @NonNull String login) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.login = login;
    }
}
