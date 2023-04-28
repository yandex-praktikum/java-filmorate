package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class FilmorateApplicationTests {

    private final UserService userService = new UserService();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private User getValidUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("validEmail@yandex.ru");
        user.setLogin("validLogin");
        user.setName("ValidName");
        user.setBirthday(LocalDate.parse("1999-12-31"));
        return user;
    }

    private Film getValidFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("ValidName");
        film.setName("ValidDescription");
        film.setReleaseDate(LocalDate.parse("1999-12-31"));
        film.setDuration(160);
        return film;
    }


    @Test
    void createUserWithInvalidEmail() {
        User user = getValidUser();
        user.setEmail("invalidEmail");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    void createUserWithInvalidLogin() {
        User user = getValidUser();
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    void createUserWithInvalidName() {
        User user = getValidUser();
        user.setName("");
        String newUserName = userService.createUser(user).getLogin();
        assertEquals(user.getLogin(), newUserName);
    }

    @Test
    void createUserWithInvalidBirthday() {
        User user = getValidUser();
        user.setBirthday(LocalDate.MAX);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    void createFilmWithInvalidName() {
        Film film = getValidFilm();
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    void createFilmWithInvalidDescription() {
        Film film = getValidFilm();
        film.setDescription("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    void createFilmWithInvalidReleaseDate() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    void createFilmWithInvalidDuration() {
        Film film = getValidFilm();
        film.setDuration(-2);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
