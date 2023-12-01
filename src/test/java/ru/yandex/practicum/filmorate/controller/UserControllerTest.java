package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();
    User expectedUser;
    User expectedUser1;

    @Test
    void isValidUser() throws ValidationException {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.addUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser, actualUser, "Пользователь не добавлен");
    }

    @Test
    void isValidEmailUserBlank() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email(" ")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        expectedUser1 = User.builder()
                .name("user")
                .login("login")
                .email("")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.addUser(expectedUser);
            userController.addUser(expectedUser1);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidEmailWithSpecialSymbol() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("useryandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.addUser(expectedUser);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidLoginUserBlank() {
        expectedUser = User.builder()
                .name("user")
                .login("")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();
        expectedUser1 = User.builder()
                .name("user")
                .login(" ")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.addUser(expectedUser);
            userController.addUser(expectedUser1);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void isValidNameUserBlank() throws ValidationException {
        expectedUser = User.builder()
                .name(" ")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.addUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidNameUserNull() throws ValidationException {
        expectedUser = User.builder()
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.addUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidNameUserEmpty() throws ValidationException {
        expectedUser = User.builder()
                .name("")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2000-12-12"))
                .build();

        userController.addUser(expectedUser);
        User actualUser = userController.getUsers().get(0);

        assertEquals(expectedUser.getName(), actualUser.getName(),
                "Пустой логин не заменен на имя пользователя");
    }

    @Test
    void isValidBirthdayUser() {
        expectedUser = User.builder()
                .name("user")
                .login("login")
                .email("user@yandex.ru")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.addUser(expectedUser);
        });
        assertNotNull(thrown.getMessage());
    }
}