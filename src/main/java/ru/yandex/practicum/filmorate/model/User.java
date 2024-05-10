package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Login не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Login не должен содержать пробелы")
    private String login;

    private String name;

    private LocalDate birthday;
}