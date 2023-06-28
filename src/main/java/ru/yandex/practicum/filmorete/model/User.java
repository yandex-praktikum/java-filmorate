package ru.yandex.practicum.filmorete.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthday;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    @Email
    private String email;
}
