package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.exception.UserValidatorException;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.filmorate.validator.exception.UserValidatorException.*;

public class UserValidator {
    public static void validate(@NonNull User user) throws UserValidatorException {
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthDay = user.getBirthday();

        if (email == null || !email.contains("@")) {
            throw new UserValidatorException(
                    String.format(INCORRECT_EMAIL, email));
        }

        if (login == null || login.indexOf(' ') >= 0) {
            throw new UserValidatorException(
                    String.format(INCORRECT_LOGIN, login));
        }

        if (name == null || name.isBlank()) {
            user.setName(login);
        }

        if (birthDay.isAfter(LocalDate.now())) {
            throw new UserValidatorException(
                    String.format(INCORRECT_BIRTHDAY, birthDay.format(ISO_DATE)));
        }
    }
}