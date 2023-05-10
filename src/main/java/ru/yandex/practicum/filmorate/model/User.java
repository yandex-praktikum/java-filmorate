package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDate;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
public class User {

    int id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;

    public boolean isEmptyName() {
        if (name == null) {
            return true;
        } else {
            return false;
        }
    }
}
