package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void filmControllerInit() {
        filmController = new FilmController();
    }

    @Test
    void createShouldThrowValidationException_ifIncorrectReleaseDate() {
        Film film1 = new Film();
        film1.setReleaseDate(LocalDate.of(1700, 11, 15));

        Film film2 = new Film();
        film2.setReleaseDate(LocalDate.of(1895, 12, 27));

        Film film3 = new Film();
        film3.setReleaseDate(LocalDate.of(1895, 12, 28));

        Film film4 = new Film();
        film4.setReleaseDate(LocalDate.of(1999, 1, 2));

       Assertions.assertDoesNotThrow(() -> filmController.addFilm(film3));
        Assertions.assertDoesNotThrow(() -> filmController.addFilm(film4));

    }

}