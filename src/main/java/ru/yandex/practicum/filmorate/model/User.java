package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {

    private int id;
    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email can't be empty")
    private String email;
    @Pattern(regexp = "^\\S*", message = "There is a space")
    @NotBlank(message = "Login can't be blank")
    private String login;
    private String name;

    @NotNull(message = "The birthday can't be empty")
    @PastOrPresent(message = "The birthday has to be before today")
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
