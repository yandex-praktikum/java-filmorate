package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

}
