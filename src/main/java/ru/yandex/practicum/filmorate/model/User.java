package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    final private Long id;
    @Email
    @NotBlank
    final private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$")
    final private String login;
    @NotNull
    final private String name;
    @PastOrPresent
    final private LocalDate birthday;
}
