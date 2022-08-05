package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.controllers.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class FilmTests {
    @Test
    void LoginWithSpace(){
        FilmController filmController= new FilmController();
        Film film=new Film(1,"Хороший фильм","Происходит только хорошее",LocalDate.of(1895,12,28),60);
        Assertions.assertThrows(ValidationException.class,
                () -> filmController.createFilm(film),

                "Ошибка валидации фильма!");
    }
}
