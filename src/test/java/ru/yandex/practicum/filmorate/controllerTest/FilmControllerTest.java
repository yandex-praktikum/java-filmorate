package ru.yandex.practicum.filmorate.controllerTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @SneakyThrows
    @Test
    void addFilm() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        final Map<Long, Film> films = filmController.findAllForTest();

        assertNotNull(films, "Список фильмов не возвращается.");
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
        assertEquals(film1, films.get(filmId), "Фильмы не совпадают");
    }

    @Test
    void addFilmWithIdAlreadyExist() {
        Film film1 = new Film("name", null, "description", LocalDate.of(1967, 03, 25), 100);
        final Long filmId = filmController.create(film1).getId();

        Film film2 = new Film("name", filmId, "description", LocalDate.of(1967, 03, 25), 100);
        final IdAlreadyExistException exception = assertThrows(
                IdAlreadyExistException.class,
                () -> filmController.create(film2));

        assertEquals("Ошибка IdAlreadyExistException: такой id уже существует", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void addFilmWithNameIsNull() {
        Film film1 = new Film(null, null, "description", LocalDate.of(1967, 03, 25), 100);

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.create(film1));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(0, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void addFilmWithNameIsBlank() {
        Film film1 = new Film(" ", null, "description", LocalDate.of(1967, 03, 25), 100);

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.create(film1));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(0, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void addFilmWithDescriptionMoreThan200() {

        Film film1 = new Film("name", null, "0123456789012345678901234567890123456789012345678901234567890123" +
                "4567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "789012345678901234567890123456789012345", LocalDate.of(1967, 03, 25), 100);
        final InvalidDescriptionException exception = assertThrows(
                InvalidDescriptionException.class,
                () -> filmController.create(film1));

        assertEquals("Ошибка InvalidDescriptionException: описание фильма не может быть длиннее 200 символов", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(0, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void addFilmWithDurationIsNegative() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1967, 03, 25), -100);
        final InvalidDurationException exception = assertThrows(
                InvalidDurationException.class,
                () -> filmController.create(film1));

        assertEquals("Ошибка InvalidDurationException: продолжительность фильма должна быть положительной", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(0, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void addFilmWithReleaseDateIsBeforeOldDate() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 27), 100);
        final InvalidReleaseDateException exception = assertThrows(
                InvalidReleaseDateException.class,
                () -> filmController.create(film1));

        assertEquals("Ошибка InvalidReleaseDateException: фильм слишком старый", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(0, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void updateFilm() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        Film film2 = new Film("new name", filmId, "new description", LocalDate.of(1895, 12, 28), 100);

        filmController.update(film2);
        final Map<Long, Film> films = filmController.findAllForTest();

        assertNotNull(films, "Список фильмов не возвращается");
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
        assertEquals(film2, films.get(filmId), "Фильмы не совпадают.");
    }

    @Test
    void updateFilmWithIdIsNull() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        Film film2 = new Film("new name", null, "new description", LocalDate.of(1895, 12, 28), 100);

        final InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidIdException: некорректный или пустой id", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void updateFilmWithNonExistentId() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        filmController.create(film1).getId();

        Film film2 = new Film("new name", 45687L, "new description", LocalDate.of(1895, 12, 28), 100);

        final InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidIdException: некорректный или пустой id", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void updateFilmWithNameIsNull() {
        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();

        Film film2 = new Film(null, filmId, "description", LocalDate.of(1967, 03, 25), 100);

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void updateFilmWithNameIsBlank() {
        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();

        Film film2 = new Film(" ", filmId, "description", LocalDate.of(1967, 03, 25), 100);

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(1, films.size(), "Неверное количество фильмов в списке");
    }

    @Test
    void updateFilmWithDescriptionMoreThan200() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();

        Film film2 = new Film("name", filmId, "0123456789012345678901234567890123456789012345678901234567890123" +
                "4567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "789012345678901234567890123456789012345", LocalDate.of(1967, 03, 25), 100);

        final InvalidDescriptionException exception = assertThrows(
                InvalidDescriptionException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidDescriptionException: описание фильма не может быть длиннее 200 символов", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(film1, films.get(filmId), "Фильмы не совпадают");
    }

    @Test
    void updateFilmWithDurationIsNegative() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        Film film2 = new Film("name", filmId, "description", LocalDate.of(1967, 03, 25), -100);

        final InvalidDurationException exception = assertThrows(
                InvalidDurationException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidDurationException: продолжительность фильма должна быть положительной", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(film1, films.get(filmId), "Фильмы не совпадают");
    }

    @Test
    void updateFilmWithReleaseDateIsBeforeOldDate() {

        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        Film film2 = new Film("name", filmId, "description", LocalDate.of(1895, 12, 27), 100);
        final InvalidReleaseDateException exception = assertThrows(
                InvalidReleaseDateException.class,
                () -> filmController.update(film2));

        assertEquals("Ошибка InvalidReleaseDateException: фильм слишком старый", exception.getMessage());

        final Map<Long, Film> films = filmController.findAllForTest();
        assertEquals(film1, films.get(filmId), "Фильмы не совпадают");
    }

    @Test
    void findAllFilms(){
        Film film1 = new Film("name", null, "description", LocalDate.of(1895, 12, 28), 100);
        final Long filmId = filmController.create(film1).getId();
        film1.setId(filmId);

        Film film2 = new Film("name2", null, "description2", LocalDate.of(1895, 12, 28), 100);
        final Long filmId2 = filmController.create(film2).getId();
        film2.setId(filmId2);

        final Map<Long, Film> films = filmController.findAllForTest();

        assertNotNull(films, "Список фильмов не возвращается.");
        assertEquals(2, films.size(), "Неверное количество фильмов в списке");
        assertEquals(film1, films.get(filmId), "Фильмы не совпадают");
        assertEquals(film2, films.get(filmId2), "Фильмы не совпадают");
    }

}
