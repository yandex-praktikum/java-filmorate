package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        User user = new User();
        user.setLogin("user1");
        user.setEmail("user1@example.com");
        user.setName("User One");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userController.create(user);

        assertNotNull(createdUser.getId());
        assertEquals("user1", createdUser.getLogin());
        assertEquals("user1@example.com", createdUser.getEmail());
    }

    @Test
    void createUser_WithInvalidEmail_ShouldThrowValidationException() {
        User user = new User();
        user.setLogin("user1");
        user.setEmail("invalid-email");
        user.setName("User One");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        User user = new User();
        user.setLogin("user1");
        user.setEmail("user1@example.com");
        user.setName("User One");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userController.create(user);

        createdUser.setName("Updated User One");
        User updatedUser = userController.update(createdUser);

        assertEquals("Updated User One", updatedUser.getName());
    }

    @Test
    void updateUser_WithNonExistentId_ShouldThrowValidationException() {
        User user = new User();
        user.setId(999L); // Не существующий ID
        user.setLogin("user1");
        user.setEmail("user1@example.com");
        user.setName("User One");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.update(user));
        assertEquals("Пользователь с id = 999 не найден", exception.getMessage());
    }
}
