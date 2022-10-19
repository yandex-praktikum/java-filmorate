package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.constants.Constants.MIN_ALLOWED_DATE;
import static ru.yandex.practicum.filmorate.constants.Constants.DATE_BEFORE_MIN_ALLOWED;
import static ru.yandex.practicum.filmorate.validation.Validation.filmValidate;


@RestController
public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void filmController(){
        filmController = new FilmController();
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
                () -> filmController.updateFilm(film));

        assertEquals("Фильм с id=9999 не найден", exception.getMessage());
    }

    public void checkException(Film film, String message){
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidate(film));

        assertEquals(message, exception.getMessage());
    }

}
