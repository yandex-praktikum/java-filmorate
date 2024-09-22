package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public static boolean isValidate(User user) {
        return (checkMail(user) && checkLogin(user) && checkName(user) && checkBirthday(user));
    }

    private static boolean checkMail(User user) {
        if (!user.getEmail().isEmpty() && user.getEmail().contains("@"))
            return true;
        else
            throw new ValidationException("Почта пустая или не содержит \"@\"");
    }

    private static boolean checkLogin(User user) {
        if (!user.getLogin().isBlank() && !user.getLogin().contains(" "))
            return true;
        else
            throw new ValidationException("Логин не может быть пустым или содержит пробелы");
    }

    private static boolean checkName(User user) {
        if (user.getName().isBlank())
            user.setName(user.getLogin());
        return true;
    }

    private static boolean checkBirthday(User user) {
        if (!user.getBirthday().isAfter(LocalDate.now()))
            return true;
        else
            throw new ValidationException("Привет из будущего!)");
    }
}