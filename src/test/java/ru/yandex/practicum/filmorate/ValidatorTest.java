package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

        Film film = new Film();
        FilmController filmController = new FilmController();
        User user = new User();
        UserController userController = new UserController();

    @Test
    public void createFilm() throws ValidationException {
        film.setName(Optional.of("Film"));
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 10, 11));
        film.setDuration(100);

        filmController.create(film);

        assertEquals(filmController.findAll().size(), 1);
    }

    @Test
    public void updateFilm() throws ValidationException {
        film.setName(Optional.of("Film"));
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2020, 10, 11));
        film.setDuration(100);

        filmController.create(film);
        Film film1 = new Film();
        film1.setName(Optional.of("Film1"));
        film1.setDescription("description1");
        film1.setReleaseDate(LocalDate.of(2020, 10, 11));
        film1.setDuration(100);
        film1.setId(1);

        filmController.update(film1);

        assertTrue(filmController.findAll().contains(film1));
    }

    @Test
    public void EmptyFilmName(){
        film.setDescription("description");
        film.setName(Optional.of(""));
        film.setReleaseDate(LocalDate.of(2020, 10, 11));
        film.setDuration(100);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("The name of the film can't be empty.", exception.getMessage());
    }

    @Test
    public void filmTooLongDescription(){

            film.setName(Optional.of("Film"));
        film.setDescription("descrikkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj" +
                "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppption");
        film.setReleaseDate(LocalDate.of(2020, 10, 11));
        film.setDuration(100);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("The film description is too long.", exception.getMessage());
    }

    @Test
    public void filmWrongReleaseDate(){
        film.setDescription("description");
        film.setName(Optional.of("Film"));
        film.setReleaseDate(LocalDate.of(1020, 10, 11));
        film.setDuration(100);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("The release data of the film can't be before 28.12.1895 and after today", exception.getMessage());
    }

    @Test
    public void filmWrongDuration(){
        film.setDescription("description");
        film.setName(Optional.of("Film"));
        film.setReleaseDate(LocalDate.of(2020, 10, 11));
        film.setDuration(-100);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("The film duration can't be under 0.", exception.getMessage());
    }

    @Test
    public void createUser() throws ValidationException {
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Login"));
        user.setEmail(Optional.of("sdlf@mail.ru"));

        userController.create(user);

        assertEquals(userController.findAll().size(), 1);
    }

    @Test
    public void updateUser() throws ValidationException {
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Login"));
        user.setEmail(Optional.of("sdlf@mail.ru"));
        userController.create(user);

        User user1 = new User();
        user1.setName(Optional.of("Name1"));
        user1.setBirthday(LocalDate.of(1991, 10, 11));
        user1.setLogin(Optional.of("Login1"));
        user1.setEmail(Optional.of("sdlf@mail.ru"));
        user1.setId(1);
        userController.update(user1);

        assertTrue(userController.findAll().contains(user1));
    }

    @Test
    public void userEmptyEmail(){
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Login"));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("The email is incorrect.", exception.getMessage());
    }

    @Test
    public void userWrongEmail(){
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Login"));
        user.setEmail(Optional.of("sdlfmail.ru"));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("The email is incorrect.", exception.getMessage());
    }

    @Test
    public void userEmptyLogin(){
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));

        user.setEmail(Optional.of("sdlf@mail.ru"));


        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("The login can't be empty and contain spaces", exception.getMessage());
    }

    @Test
    public void userLoginWithSpaces(){
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Log  in"));
        user.setEmail(Optional.of("sdlf@mail.ru"));


        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("The login can't be empty and contain spaces", exception.getMessage());
    }

    @Test
    public void userWrongBirthday(){
        user.setName(Optional.of("Name"));
        user.setBirthday(LocalDate.of(2990, 10, 11));
        user.setLogin(Optional.of("Login"));
        user.setEmail(Optional.of("sdlf@mail.ru"));


        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("The user's birthday can't be in the future", exception.getMessage());
    }

    @Test
    public void userEmptyWithName() throws ValidationException {
        user.setBirthday(LocalDate.of(1990, 10, 11));
        user.setLogin(Optional.of("Login"));
        user.setEmail(Optional.of("sdlf@mail.ru"));

        userController.create(user);

        User user1 = new User();
        user1.setName(Optional.of("Login"));
        user1.setBirthday(LocalDate.of(1990, 10, 11));
        user1.setLogin(Optional.of("Login"));
        user1.setEmail(Optional.of("sdlf@mail.ru"));
        user1.setId(1);

        assertTrue(userController.findAll().contains(user1));
    }



    }


