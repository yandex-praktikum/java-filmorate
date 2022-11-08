package filmorate.models;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data

public class User {
    private int id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null) ? login : name;
        this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE);
    }
}
