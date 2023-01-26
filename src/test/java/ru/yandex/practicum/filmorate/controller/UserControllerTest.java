package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private static UserController userController;
    private User user;

    @BeforeAll
    public static void createController() {
        userController = new UserController();
    }

    @BeforeEach
    public void createFilm() {
        user = new User(0, "aydarhub@yandex.ru", "login", "name",
                LocalDate.of(1999, Month.DECEMBER, 11));
    }

    @Test
    void shouldExceptionWithNull() {
        ValidationException e = assertThrows(ValidationException.class, () -> userController.create(null));
        assertEquals("Пользователь не может быть null", e.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyLogin() {
        user.setLogin("");
        ValidationException e = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("логин не может быть пустым и содержать пробелы", e.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectLogin() {
        user.setLogin("log in");
        ValidationException e = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("некорректный логин", e.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectEmail() {
        user.setEmail("ddd.dddf@dddd");
        ValidationException e = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("некорректный email", e.getMessage());
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        user.setId(10);
        ValidationException e = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Пользователь с таким id не существует", e.getMessage());
    }

    @Test
    void shouldNotExceptionWithEmptyName() {
        user.setName("");
        userController.create(user);
        assertEquals(user.getName(), user.getLogin());
    }

}