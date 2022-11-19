package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

        Film film = new Film(0, "Film", LocalDate.of(2020, 10, 11),
                60, "Film");

    Film film1 = new Film(1, "Film1", LocalDate.of(2010, 10, 11),
            60, "Film1");
        FilmController filmController = new FilmController();
        User user = new User(0, "Name", LocalDate.of(1990, 10, 11),
                "fkg@mail.ru", "Login");
        User user1 = new User(141, "Name2", LocalDate.of(1990, 10, 11),
                "fkg@mail.ru", "Login2");
        UserController userController = new UserController();

    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    @Test
    public void createFilm() throws ValidationException {
        filmController.create(film);

        assertEquals(filmController.findAll().size(), 1);
    }

    @Test
    public void updateFilm() throws ValidationException {

        filmController.create(film);

        filmController.update(film1);

        assertTrue(filmController.findAll().contains(film1));
    }

    @Test
    public void EmptyFilmName(){
        film.setName("");

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void filmTooLongDescription(){

        film.setDescription("descrikkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj" +
                "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppption");

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void filmWrongReleaseDate(){

        film.setReleaseDate(LocalDate.of(1020, 10, 11));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void filmWrongDuration(){

        film.setDuration(-100);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void createUser() throws ValidationException {
        userController.create(user);

        assertEquals(userController.findAll().size(), 1);
    }

    @Test
    public void updateUser() throws ValidationException {

        userController.create(user);
        userController.update(user1);

        assertTrue(userController.findAll().contains(user1));
    }

    @Test
    public void userWrongEmail(){

        user.setEmail("sdlfmail.ru");

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void userEmptyLogin(){
        user.setLogin("");

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void userWrongBirthday(){
        user.setBirthday(LocalDate.of(2990, 10, 11));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
        assertEquals("Error while saving", exception.getMessage());
    }

    @Test
    public void userEmptyWithName() throws ValidationException {
        user.setName("");

        userController.create(user);

        User user2 = new User(user.getId(), user.getLogin(), user.getBirthday(),
                user.getEmail(), user.getLogin());

        assertTrue(userController.findAll().contains(user2));
    }
    }


