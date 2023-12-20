package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Valid
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

    private final Set<Integer> friends = new HashSet<>();

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
