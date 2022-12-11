package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
}
