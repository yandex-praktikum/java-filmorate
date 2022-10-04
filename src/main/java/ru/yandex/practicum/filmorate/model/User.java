package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @Pattern(regexp = "[^ ]+$")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
