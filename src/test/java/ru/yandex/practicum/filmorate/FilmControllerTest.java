package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RestController
public class FilmControllerTest {
    private final LocalDate TEST_DATE = LocalDate.of(1895,12,29);
    private final LocalDate MIN_DATE = LocalDate.of(1895,12,28);
    FilmController filmController;

    @BeforeEach
    void filmController(){
        filmController = new FilmController();
    }

    @Test
    public void correctValidationOfFilm() {
        Film film = new Film(0, "name", "d".repeat(200), TEST_DATE, 1);
        assertEquals("name", film.getName());
        assertEquals("d".repeat(200), film.getDescription());
        assertEquals(TEST_DATE, film.getReleaseDate());
        assertEquals(1, film.getDuration());
    }

    @Test
    public void releaseDateMustBeMoreThanMinimumAllowedDate() {
        Film film = new Film(0, "n", "d", MIN_DATE, 1);
        checkException(film, "Дата релиза должна быть больше 28.12.1895");
    }

    @Test
    public void durationOfTheFilmShouldBePositive() {
        Film film = new Film(0, "film_name", "description", TEST_DATE, 0);
        checkException(film, "Продолжительность фильма должна быть положительной");
    }

    @Test
    public void nameCanNotBeEmptyOrNull() {
        Film film = new Film(0, "", "description", TEST_DATE, 111);
        checkException(film, "Название не может быть пустым или null");

        Film film1 = new Film(0, null, "description", TEST_DATE, 111);
        checkException(film1, "Название не может быть пустым или null");
    }

    @Test
    public void descriptionShouldNotBeMoreThan200Characters() {
        Film film = new Film(0, "film_name", "d".repeat(201), TEST_DATE, 111);
        checkException(film, "Описание не должно превышать 200 символов");
    }

    @Test
    public void updateFilmWithAnUnknownId() {
        Film film = new Film(9999, "film_name", "description", TEST_DATE, 111);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film));

        assertEquals("Фильм с id=9999 не найден", exception.getMessage());
    }

    public void checkException(Film film, String message){
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.validate(film));

        assertEquals(message, exception.getMessage());
    }

}
