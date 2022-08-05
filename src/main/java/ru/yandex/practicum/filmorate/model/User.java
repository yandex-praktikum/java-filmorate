package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    @Min(1)
    private final int id;
    private String name;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @PastOrPresent
    private final LocalDate birthday;
}
