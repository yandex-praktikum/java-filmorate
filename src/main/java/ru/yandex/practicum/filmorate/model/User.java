package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    int id;
    @NotNull(message = "Электронная почта не может быть пустой.")
    @Email(message = "Электронная почта должна быть корректного формата.")
    String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^[\\S]+$", message = "Логин не может содержать пробелы.")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть больше текущей даты.")
    Date birthday;

}
