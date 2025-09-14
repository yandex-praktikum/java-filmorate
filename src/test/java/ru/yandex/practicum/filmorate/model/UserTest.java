package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private UserController userController;
    private User validUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testuser");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldCreateValidUser() {
        User createdUser = userController.createUser(validUser);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("testuser", createdUser.getLogin());
        assertTrue(createdUser.getId() > 0);
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsBlank() {
        validUser.setName("");
        User createdUser = userController.createUser(validUser);

        assertEquals("testuser", createdUser.getName());
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsNull() {
        validUser.setName(null);
        User createdUser = userController.createUser(validUser);

        assertEquals("testuser", createdUser.getName());
    }

    @Test
    void shouldPreserveNameWhenNameIsProvided() {
        validUser.setName("Test User");
        User createdUser = userController.createUser(validUser);

        assertEquals("Test User", createdUser.getName());
    }

    @Test
    void shouldGetAllUsers() {
        userController.createUser(validUser);
        Collection<User> users = userController.getAllUsers();

        assertEquals(1, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("testuser")));
    }

    @Test
    void shouldUpdateUser() {
        User createdUser = userController.createUser(validUser);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(1995, 1, 1));

        User result = userController.updateUser(updatedUser);

        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updateduser", result.getLogin());
        assertEquals("Updated User", result.getName());
        assertEquals(createdUser.getId(), result.getId());
    }

    @Test
    void shouldGenerateUniqueIds() {
        User user1 = userController.createUser(validUser);

        User user2 = new User();
        user2.setEmail("second@example.com");
        user2.setLogin("seconduser");
        user2.setBirthday(LocalDate.of(1995, 1, 1));
        User createdUser2 = userController.createUser(user2);

        assertNotEquals(user1.getId(), createdUser2.getId());
        assertEquals(2, userController.getAllUsers().size());
    }
}
