package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    int id;
    @NotBlank
    String login;
    String name;
    @NotBlank
    @Email
    String email;
    @Past
    LocalDate birthday;
}
