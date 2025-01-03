package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Test
    void shouldCorrect() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertDoesNotThrow(() -> new FilmController().createFilm(film),
                "Ожидалось, что createFilm() не выдаст исключение, но оно произошло.");
    }

    @Test
    void shouldFailEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> new FilmController().createFilm(film),
                "Ожидалось, что createFilm() выдаст исключение, но этого не произошло."
        );
        assertTrue(thrown.getMessage().contains("Должно быть задано название фильма"));
    }

    @Test
    void shouldFailLongDescription() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This description is way too long ".repeat(10));
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> new FilmController().createFilm(film),
                "Ожидалось, что createFilm() выдаст исключение, но этого не произошло."
        );
        assertTrue(thrown.getMessage().contains("Описание фильма не может быть длиннее 200 символов"));
    }

    @Test
    void shouldFailEarlyReleaseDate() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(1890, 1, 1));
        film.setDuration(120);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> new FilmController().createFilm(film),
                "Ожидалось, что createFilm() выдаст исключение, но этого не произошло."
        );
        assertTrue(thrown.getMessage().contains("Фильм не может быть старше 28 декабря 1895 года"));
    }

    @Test
    void shouldFailNonPositiveDuration() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(-10);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> new FilmController().createFilm(film),
                "Ожидалось, что createFilm() выдаст исключение, но этого не произошло."
        );
        assertTrue(thrown.getMessage().contains("Длительность фильма может быть только положительной"));
    }
}
