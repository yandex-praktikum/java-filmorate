package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}