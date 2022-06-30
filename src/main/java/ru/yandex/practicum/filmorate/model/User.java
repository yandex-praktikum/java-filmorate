package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private static int count;
    private int id;
    @NotEmpty
    @Email
    private final String email;
    @NotEmpty
    @NotBlank
    private final String login;
    private String name = null;
    @Past
    private final LocalDate birthday;

    public void generateId() {
        if(id == 0) {
            id = ++count;
        }
    }
}
