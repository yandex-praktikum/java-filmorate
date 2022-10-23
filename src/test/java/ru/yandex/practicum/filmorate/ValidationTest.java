package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.constants.Constants.DATE_BEFORE_MIN_ALLOWED;
import static ru.yandex.practicum.filmorate.constants.Constants.MIN_ALLOWED_DATE;
import static ru.yandex.practicum.filmorate.validation.Validation.validateFilm;
import static ru.yandex.practicum.filmorate.validation.Validation.validateUser;

public class ValidationTest {
    private final LocalDate BIRTHDAY_DATE = LocalDate.now();

    @Test
    public void correctValidationOfUser() {
        User user = new User(0, "email@e.ru", "login", "name", BIRTHDAY_DATE);
        assertEquals("email@e.ru", user.getEmail());
        assertEquals("login", user.getLogin());
        assertEquals("name", user.getName());
        assertEquals(BIRTHDAY_DATE, user.getBirthday());
    }

    @Test
    public void correctValidationOfFilm() {
        Film film = new Film(0, "name", "d".repeat(200), MIN_ALLOWED_DATE, 1);
        assertEquals("name", film.getName());
        assertEquals("d".repeat(200), film.getDescription());
        assertEquals(MIN_ALLOWED_DATE, film.getReleaseDate());
        assertEquals(1, film.getDuration());
    }

    @Test
    public void emailCanNotBeEmptyAndMustContainMailSymbol() {
        User user = new User(0, "", "login", "name", BIRTHDAY_DATE);
        checkException(user, "Почта не может быть пустой и должна содержать символ @");

        User user1 = new User(0, "emaile.ru", "login", "name", BIRTHDAY_DATE);
        checkException(user1, "Почта не может быть пустой и должна содержать символ @");
    }

    @Test
    public void loginCanNotContainSpacesOrNotEmptyOrNotNull() {
        User user = new User(0, "email@e.ru", "     ", "name", BIRTHDAY_DATE);
        checkException(user, "Логин не может быть пустым и содержать пробелы");

        User user1 = new User(0, "email@e.ru", null, "name", BIRTHDAY_DATE);
        checkException(user1, "Логин не может быть пустым и содержать пробелы");

        User user2 = new User(0, "email@e.ru", "", "name", BIRTHDAY_DATE);
        checkException(user2, "Логин не может быть пустым и содержать пробелы");
    }

    @Test
    public void emptyOrNullNameShouldBeReplacedWithLogin() {
        User user = new User(0, "email@e.ru", "login123", "", BIRTHDAY_DATE);
        validateUser(user);
        assertEquals("login123", user.getName());

        User user1 = new User(1, "emaill@e.ru", "login1234", null, BIRTHDAY_DATE);
        validateUser(user1);
        assertEquals("login1234", user1.getName());
    }

    @Test
    public void dateOfBirthdayShouldNotBeInTheFuture() {
        User user = new User(0, "email@e.ru", "login", "name",
                BIRTHDAY_DATE.plusYears(20000));
        checkException(user, "Дата рождения не может быть в будущем");
    }

    @Test
    public void updateUserWithAnUnknownId() {
        User user = new User(9999, "email@e.ru", "login123", "name", BIRTHDAY_DATE);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new InMemoryUserStorage(List.of()).updateUser(user));

        assertEquals("Пользователь с id=9999 не найден", exception.getMessage());
    }

    @Test
    public void releaseDateMustBeEqualOrMoreMinimumAllowedDate() {
        Film film = new Film(0, "n", "d", DATE_BEFORE_MIN_ALLOWED, 1);
        checkException(film, "Дата релиза должна быть не раньше 28.12.1895");
    }

    @Test
    public void durationOfTheFilmShouldBePositive() {
        Film film = new Film(0, "film_name", "description", MIN_ALLOWED_DATE, 0);
        checkException(film, "Продолжительность фильма должна быть положительной");
    }

    @Test
    public void nameCanNotBeEmptyOrNull() {
        Film film = new Film(0, "", "description", MIN_ALLOWED_DATE, 111);
        checkException(film, "Название не может быть пустым или null");

        Film film1 = new Film(0, null, "description", MIN_ALLOWED_DATE, 111);
        checkException(film1, "Название не может быть пустым или null");
    }

    @Test
    public void descriptionShouldNotBeMoreThan200Characters() {
        Film film = new Film(0, "film_name", "d".repeat(201), MIN_ALLOWED_DATE, 111);
        checkException(film, "Описание не должно превышать 200 символов");
    }

    @Test
    public void updateFilmWithAnUnknownId() {
        Film film = new Film(9999, "film_name", "description", MIN_ALLOWED_DATE, 111);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new InMemoryFilmStorage(List.of()).updateFilm(film));

        assertEquals("Фильм с id=9999 не найден", exception.getMessage());
    }

    public void checkException(Film film, String message){
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validateFilm(film));

        assertEquals(message, exception.getMessage());
    }

    public void checkException(User user, String message){
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validateUser(user));

        assertEquals(message, exception.getMessage());
    }
}
