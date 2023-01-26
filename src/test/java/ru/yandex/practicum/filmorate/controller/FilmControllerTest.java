package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static FilmController filmController;
    private Film film;

    @BeforeAll
    public static void createController() {
        filmController = new FilmController();
    }

    @BeforeEach
    public void createFilm() {
        film = new Film(0, "name", "description", LocalDate.of(2005, Month.MAY, 15), 100);
    }

    @Test
    void shouldExceptionWithNull() {
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.create(null));
        assertEquals("фильм не может быть null", e.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyName() {
        film.setName("");
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("название не может быть пустым", e.getMessage());
    }

    @Test
    void shouldExceptionWithTooLongDescription() {
        film.setDescription("a".repeat(201));
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Максимальная длина описания - 200 символов", e.getMessage());
    }

    @Test
    void shouldExceptionWithNegativeDuration() {
        film.setDuration(-1);
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectReleaseDay() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 15));
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата релиза не должно быть раньше 28 декабря 1895 года", e.getMessage());
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        film.setId(10);
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Фильм с таким id не существует", e.getMessage());
    }
}