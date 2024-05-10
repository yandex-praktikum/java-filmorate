package ru.yandex.practicum.filmorate.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class UserServiceImplTest {

    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl();
    }

    @Test
    public void whenCreateUser_thenUserIsAddedWithNewId() {
        User user = new User(1L, "user@example.com","login", "username",
                LocalDate.of(1990, 1, 1));
        User result = userService.createUser(user);

        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals("user@example.com", result.getEmail());
        assertEquals("username", result.getName());
        assertEquals("login", result.getLogin());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthday());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    public void whenUpdateExistingUser_thenUserIsUpdated() {
        User user = new User(1L, "user@example.com", "username", "login", LocalDate.of(1990, 1, 1));
        User createdUser = userService.createUser(user);
        createdUser.setEmail("newuser@example.com");
        User updatedUser = userService.updateUser(createdUser.getId(), createdUser);

        assertNotNull(updatedUser);
        assertEquals("newuser@example.com", updatedUser.getEmail());
        assertEquals(createdUser.getId(), updatedUser.getId());
    }

    @Test
    public void whenUpdateNonExistingUser_thenNoUserIsUpdated() {
        User user = new User(1L, "user@example.com", "username", "login",
                LocalDate.of(1990, 1, 1));
        User updatedUser = userService.updateUser(999L, user);

        assertNull(updatedUser);
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    public void whenGetAllUsers_thenAllUsersAreReturned() {
        userService.createUser(new User(1L, "user1@example.com", "user1", "name1", LocalDate.of(1990, 1, 1)));
        userService.createUser(new User(1L, "user2@example.com", "user2", "name2", LocalDate.of(1995, 5, 15)));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getLogin().equals("user1")));
        assertTrue(users.stream().anyMatch(user -> user.getLogin().equals("user2")));
    }

}