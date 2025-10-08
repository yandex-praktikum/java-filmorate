package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    Long id;
    @Email(message = "Введенный email не соответствует формату email-адресов!")
    String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;
}

