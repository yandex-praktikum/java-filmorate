package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String name;
    @NotBlank
    @Email
    private final String email;
    private final String login;
    private final LocalDate birthday;
}
