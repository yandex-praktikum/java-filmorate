package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;
    private User user;

    @BeforeEach
    void setUp() {
        controller = new UserController();
        user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 5, 20));
    }

    @Test
    void shouldAddValidUser() {
        User added = controller.addUser(user);
        assertEquals(1, added.getId());
        assertEquals("Test User", added.getName());
    }

    @Test
    void shouldThrowExceptionWhenEmailInvalid() {
        user.setEmail("invalidEmail");
        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    void shouldThrowExceptionWhenLoginHasSpaces() {
        user.setLogin("bad login");
        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }

    @Test
    void shouldUseLoginAsNameWhenNameEmpty() {
        user.setName("");
        User added = controller.addUser(user);
        assertEquals(user.getLogin(), added.getName());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> controller.addUser(user));
    }
}
