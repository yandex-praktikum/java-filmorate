package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmControllerTest = new FilmController();
    Film expectedFilm;

    @Test
    void isValidNameFilms() throws ValidationException {
        expectedFilm = Film.builder()
                .name("film")
                .description("descTest")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();
        filmControllerTest.addFilm(expectedFilm);

        Film actualFilm = filmControllerTest.getFilms().get(0);

        assertEquals(expectedFilm, actualFilm, "Фильм не добавлен");
    }

    @Test
    void isValidNameFilmsBlank() {
        expectedFilm = Film.builder()
                .name("")
                .description("descTest")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmControllerTest.addFilm(expectedFilm);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidNameFilmsBlank2() {
        expectedFilm = Film.builder()
                .name(" ")
                .description("descTest")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmControllerTest.addFilm(expectedFilm);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidDescriptionFilmsMore200() {
        String expectedDescription = "A" + "a".repeat(200);
        expectedFilm = Film.builder()
                .name("film1")
                .description(expectedDescription)
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmControllerTest.addFilm(expectedFilm);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidReleaseDateFilms() {
        expectedFilm = Film.builder()
                .name("film1")
                .description("desc1")
                .releaseDate(LocalDate.parse("1895-12-26"))
                .duration(120)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmControllerTest.addFilm(expectedFilm);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidDurationFilms() {
        expectedFilm = Film.builder()
                .name("film1")
                .description("desc1")
                .releaseDate(LocalDate.parse("1895-12-28"))
                .duration(-120)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmControllerTest.addFilm(expectedFilm);
        });
        assertNotNull(thrown.getMessage());
    }
}