package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long id;
    @Email(message = "Введенный email не соответствует формату email-адресов!")
    String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();
}
