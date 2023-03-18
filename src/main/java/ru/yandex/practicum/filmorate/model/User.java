package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

@Data
@AllArgsConstructor
@ToString
public class User {
    int id;
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;

}
