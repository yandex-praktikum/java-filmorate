package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private final UserController controller = new UserController();

    @Test
    void shouldRejectEmptyEmail() {
        User user = validUser();
        user.setEmail(" ");

        assertThrows(ConditionsNotMetException.class, () -> controller.create(user));
    }

    @Test
    void shouldRejectEmailWithoutAtSign() {
        User user = validUser();
        user.setEmail("user.example.com");

        assertThrows(ConditionsNotMetException.class, () -> controller.create(user));
    }

    @Test
    void shouldRejectEmptyLogin() {
        User user = validUser();
        user.setLogin("");

        assertThrows(ConditionsNotMetException.class, () -> controller.create(user));
    }

    @Test
    void shouldUseLoginWhenNameIsBlank() {
        User user = validUser();
        user.setName(" ");

        User createdUser = controller.create(user);

        assertEquals("login", createdUser.getName());
    }

    @Test
    void shouldAcceptBirthdayToday() {
        User user = validUser();
        user.setBirthday(LocalDate.now());

        User createdUser = controller.create(user);

        assertEquals(LocalDate.now(), createdUser.getBirthday());
    }

    @Test
    void shouldRejectFutureBirthday() {
        User user = validUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ConditionsNotMetException.class, () -> controller.create(user));
    }

    @Test
    void shouldRejectUpdateWithoutId() {
        User user = validUser();

        assertThrows(ConditionsNotMetException.class, () -> controller.update(user));
    }

    @Test
    void shouldRejectUpdateForUnknownUser() {
        User user = validUser();
        user.setId(1L);

        assertThrows(NotFoundException.class, () -> controller.update(user));
    }

    private User validUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

}
