package ru.yandex.prakticum.filmorate.controllers.films.users.controller;


//электронная почта не может быть пустой и должна содержать символ @;
//логин не может быть пустым и содержать пробелы;
//        имя для отображения может быть пустым — в таком случае будет использован логин;
//        дата рождения не может быть в будущем.


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.prakticum.filmorate.controllers.films.users.controller.exceptions.ValidationException;
import ru.yandex.prakticum.filmorate.controllers.films.users.model.User;

import java.time.Instant;
import java.util.Date;

@Data
@Slf4j
public class UserCheck {
    public static boolean userCheck(User user){
        if (!emailCheck(user.getEmail())){
            return false;
        }
        if (!loginCheck(user.getLogin())){
            return false;
        }
        nicknameCheck(user);
        if (!dayOfBirthCheck(user.getBirthday())){
            return false;
        }
        return true;
    }
    private static boolean emailCheck(String email){
        if (!email.contains("@") || email.isBlank())
        {
            log.error("Email не верифицирован");
            throw new ValidationException("Email не верифицирован");
        }
            return true;
    }

    private static boolean loginCheck(String login){
        if (login.isBlank() || login.contains(" ")){
            log.error("Login не верифицирован");
            throw new ValidationException("Login не верифицирован");
        }
        return true;
    }

    private static void nicknameCheck(User user){
        if (user.getNickname().isBlank()){
            user.setNickname(user.getLogin());
            log.trace("Ник заменен нап логин");
        }
    }

    private static boolean dayOfBirthCheck(Date birthday){
        if (birthday.toInstant().isAfter(Instant.now())){
            log.error("Дата рождения не верифицирована");
            throw new ValidationException("Дата рождения не верифицирована");
        }
        return true;
    }

}
