package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@Data
public class User {
    private final long id = UserIdGenerator.generate();
    @NotNull
    @Email
    private final String email;
    @NotNull
    @NotBlank
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;

}
