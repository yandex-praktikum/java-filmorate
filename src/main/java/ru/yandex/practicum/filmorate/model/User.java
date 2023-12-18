package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Valid
@ToString(callSuper = true)
public class User extends StorageData {

    @NotBlank(message = "{email.user.not_blank}")
    @Email(message = "{email.user.not_valid}")
    private String email;

    @NotBlank(message = "{login.user.not_blank}")
    @Pattern(regexp = "^\\S*$", message = "{login.user.no_spaces}")
    private String login;

    private String name;

    @Past(message = "{birthday.user.not_future}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
