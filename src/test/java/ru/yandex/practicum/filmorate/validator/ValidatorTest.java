package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    public static UserController userController;
    public static FilmController filmController;
    public User user1 = new User("", "Maikoo",
            LocalDate.of(1991, 11, 18));
    public User user2 = new User("sunbaked", "Maikoo",
            LocalDate.of(1991, 11, 18));
    public User user3 = new User("sunbaked@list.ru", null,
            LocalDate.of(1991, 11, 18));
    public User user4 = new User("sunbaked@list.ru", "",
            LocalDate.of(1991, 11, 18));
    public User user5 = new User("sunbaked@list.ru", "Maikoo Heikoo",
            LocalDate.of(1991, 11, 18));
    public User user6 = new User("sunbaked@list.ru", "Maikoo",
            LocalDate.of(2991, 11, 18));
    public User user7 = new User(null, null,
            null);
    public Film film1 = new Film("", "Film",
            LocalDate.of(2000, 11, 11), 100);
    public Film film2 = new Film(null, "Film",
            LocalDate.of(2000, 11, 11), 100);
    public Film film3 = new Film("Film", "Film",
            LocalDate.of(1000, 11, 11), 100);
    public Film film4 = new Film("Film", "Film",
            LocalDate.of(2000, 11, 11), -1);
    public Film film5 = new Film("Film", "Компания Blizzard Entertainment уже не одно десятилетие " +
            "является гарантом качества в игровой индустрии. Созданная за более чем 20 лет вселенная мира Warcraft " +
            "успела покорить миллионы людей и продолжает завоёвывать новых поклонников. ",
            LocalDate.of(2000, 11, 11), 100);
    public Film film6 = new Film(null, null,
            null, null);


    @BeforeAll
    public static void beforeAll() {
        userController = new UserController();
        filmController = new FilmController();
    }

    @Test
    void shouldThrownValidationExceptionOnUserCreation() throws ValidationException {
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user1);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user2);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user3);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user4);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user5);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user6);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                userController.createUser(user7);
            }
        }));
        assertTrue(userController.userList().isEmpty());
    }

    @Test
    void shouldThrownValidationExceptionOnFilmCreation() {
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film1);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film2);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film3);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film4);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film5);
            }
        }));
        assertThrows(ValidationException.class, (new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.createFilm(film6);
            }
        }));
        assertTrue(filmController.filmList().isEmpty());
    }
}