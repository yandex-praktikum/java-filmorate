package model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
@Data
public class User {
    private int id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

//    электронная почта не может быть пустой и должна содержать символ @;
//    логин не может быть пустым и содержать пробелы;
//    имя для отображения может быть пустым — в таком случае будет использован логин;
//    дата рождения не может быть в будущем.
}
