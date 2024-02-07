package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validate.annotation.BirthdayValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UserDto {

    private int id;
    @NonNull
    @Email
    private String email;
    private String name;
    @NotBlank
    @Pattern(regexp = "([\\S]+)")
    private String login;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @BirthdayValidation
    private LocalDate birthday;
}
