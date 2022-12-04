package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    Film film = new Film(0, "Film", LocalDate.of(2020, 10, 11),
            60, "Film");

    Film film1 = new Film(1, "Film1", LocalDate.of(2010, 10, 11),
            60, "Film1");
    Film film2 = new Film(1, "Film2", LocalDate.of(2011, 10, 11),
            160, "Film2");
    User user = new User(1, "Name", LocalDate.of(1990, 10, 11),
            "fkg@mail.ru", "Login");
    User user1 = new User(1, "Name2", LocalDate.of(1990, 10, 11),
            "fsfg@mail.ru", "Login2");
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);
    FilmController filmController = new FilmController(filmService, inMemoryFilmStorage);

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);

    UserController userController = new UserController(userService, inMemoryUserStorage);


    @Test
    public void getPopularFilms() throws ValidationException {
        filmController.create(film);
        filmController.create(film1);
        filmController.create(film2);

        userController.create(user);
        userController.create(user1);

        filmController.setLike(film.getId(), user.getId());
        filmController.setLike(film1.getId(), user.getId());
        filmController.setLike(film1.getId(), user1.getId());

        assertEquals(3, filmController.getPopularFilms("10").size());

    }

    @Test
    public void getMostPopularFilms() throws ValidationException {
        filmController.create(film);
        filmController.create(film1);
        filmController.create(film2);

        userController.create(user);
        userController.create(user1);

        filmController.setLike(film.getId(), user.getId());
        filmController.setLike(film1.getId(), user.getId());
        filmController.setLike(film1.getId(), user1.getId());

        assertEquals(1, filmController.getPopularFilms("1").size());

    }

}
