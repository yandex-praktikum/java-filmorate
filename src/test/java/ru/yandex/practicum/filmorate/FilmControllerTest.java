package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void createFilm_ShouldReturnCreatedFilm() {
        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm.getId());
        assertEquals("Первый фильм", createdFilm.getName());
    }

    @Test
    void createFilm_WithInvalidReleaseDate_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate(LocalDate.of(1800, 1, 1)); // Неверная дата
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Некорректные данные фильма", exception.getMessage());
    }

    @Test
    void updateFilm_ShouldReturnUpdatedFilm() {
        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);
        Film createdFilm = filmController.create(film);

        createdFilm.setName("Обновленный первый фильм");
        Film updatedFilm = filmController.update(createdFilm);

        assertEquals("Обновленный первый фильм", updatedFilm.getName());
    }

    @Test
    void updateFilm_WithNonExistentId_ShouldThrowValidationException() {
        Film film = new Film();
        film.setId(999L); // Не существующий ID
        film.setName("Первый фильм");

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.update(film));
        assertEquals("Фильм с id = 999 не найден", exception.getMessage());
    }
}


