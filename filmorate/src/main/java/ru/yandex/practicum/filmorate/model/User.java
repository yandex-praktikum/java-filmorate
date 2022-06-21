package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
public class User {
    private int id;
    private String name;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    private List<User> friends;
}
