package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.Constant.REGEX_EMAIL;
import static ru.yandex.practicum.filmorate.constant.Constant.REGEX_LOGIN;

@Data
@Builder
public class User {
    private Integer id;
    @NotEmpty(message = "Почта не может быть пустая")
    @Email(regexp = REGEX_EMAIL, message = "В 'email' использованы запрещённые символы")
    private String email;
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = REGEX_LOGIN, message = "В 'login' использованы запрещённые символы")
    private String login;
    private String name;
    @Past(message = "День рождения не может быть указан в будущем")
    private LocalDate birthday;
    private Set<Integer> friends;
}
