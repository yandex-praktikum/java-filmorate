package ru.yandex.practicum.filmorate.validation;

import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.constants.Constants.MIN_ALLOWED_DATE;

public class Validation {
    public static void userValidate(User user) {
        if (!StringUtils.hasLength(user.getEmail()) || !user.getEmail().contains("@")) throw new ValidationException(
                "Почта не может быть пустой и должна содержать символ @");

        if (!StringUtils.hasLength(user.getLogin()) || user.getLogin().contains(" ")) throw new ValidationException(
                "Логин не может быть пустым и содержать пробелы");

        if (user.getBirthday().isAfter(LocalDate.now())) throw new ValidationException("Дата рождения не может быть " +
                "в будущем");

        if (!StringUtils.hasLength(user.getName())) user.setName(user.getLogin());
    }

    public static void filmValidate(Film film) {
        if (!StringUtils.hasLength(film.getName())) throw new ValidationException("Название не может быть пустым " +
                "или null");
        if (film.getDescription().length() > 200) throw new ValidationException("Описание не должно превышать 200 " +
                "символов");
        if (film.getReleaseDate().isBefore(MIN_ALLOWED_DATE)) throw new ValidationException("Дата релиза должна быть не " +
                "раньше 28.12.1895");
        if (film.getDuration() <= 0) throw new ValidationException("Продолжительность фильма должна быть " +
                "положительной");
    }
}
