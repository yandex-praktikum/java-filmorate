package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    @PositiveOrZero
    private int id;
    @Email
    private String email;
    @NotNull
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
