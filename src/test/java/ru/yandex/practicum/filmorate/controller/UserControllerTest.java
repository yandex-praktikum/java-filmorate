package ru.yandex.practicum.filmorate.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private Map<Integer, User> users;

    @BeforeEach
    public void setUp() {
        users = new HashMap<>();
        userController = new UserController();
    }

    @Test
    void findAll() {
        User user = new User();
        user.setLogin("User");
        user.setName("Name");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        User user2 = new User();
        user2.setLogin("User2");
        user2.setName("Name2");
        user2.setEmail("User2@email.com");
        user2.setBirthday(LocalDate.of(2001, 11, 14));
        User addedUser = userController.create(user);
        User addedUser2 = userController.create(user2);
        Collection<User> users = userController.findAll();
        Collection<User> usersExp = List.of(user, user2);
        Assertions.assertThat(users).containsExactlyElementsOf(usersExp);
    }

    @Test
    void createUser() {
        User user = new User();
        user.setLogin("User");
        user.setName("Name");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        User addedUser = userController.create(user);
        assertNotNull(addedUser);
        assertEquals("User", addedUser.getLogin());
        assertEquals("Name", addedUser.getName());
        assertEquals("User@email.com", addedUser.getEmail());
        assertEquals(LocalDate.of(2000, 12, 11), addedUser.getBirthday());
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setLogin("User");
        user.setName("Name");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        User addedUser = userController.create(user);
        assertNotNull(addedUser);
        User newUser = new User();
        newUser.setId(1);
        newUser.setLogin("UserEDITED");
        newUser.setName("NameEDITED");
        newUser.setEmail("UserEDITED@email.com");
        newUser.setBirthday(LocalDate.of(2005, 6, 25));
        User updatedUser = userController.update(newUser);
        assertNotNull(updatedUser);
        assertEquals("UserEDITED", updatedUser.getLogin());
        assertEquals("NameEDITED", updatedUser.getName());
        assertEquals("UserEDITED@email.com", updatedUser.getEmail());
        assertEquals(LocalDate.of(2005, 6, 25), updatedUser.getBirthday());
    }

    @Test
    void createUserNoName() {
        User user = new User();
        user.setLogin("Login");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        User addedUser = userController.create(user);
        assertEquals("Login", addedUser.getName());
    }

    @Test
    void createUserWithErrors() {
        User user = new User();
        user.setLogin("");
        user.setName("Name");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown1 = assertThrows(
                ValidationException.class,
                () -> userController.create(user),
                "Ожидалось исключение ValidationException для пустого логина"
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", thrown1.getMessage());
        User user2 = new User();
        user2.setLogin("Login Log");
        user2.setName("Name");
        user2.setEmail("User@email.com");
        user2.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown2 = assertThrows(
                ValidationException.class,
                () -> userController.create(user2),
                "Ожидалось исключение ValidationException для логина с пробелами"
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", thrown2.getMessage());
        User user3 = new User();
        user3.setLogin("Login");
        user3.setName("Name");
        user3.setEmail("");
        user3.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown3 = assertThrows(
                ValidationException.class,
                () -> userController.create(user3),
                "Ожидалось исключение ValidationException для пустой почты"
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown3.getMessage());
        User user4 = new User();
        user4.setLogin("Login");
        user4.setName("Name");
        user4.setEmail("Email");
        user4.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown4 = assertThrows(
                ValidationException.class,
                () -> userController.create(user4),
                "Ожидалось исключение ValidationException для неверной почты без @"
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown4.getMessage());
        User user5 = new User();
        user5.setLogin("Login");
        user5.setName("Name");
        user5.setEmail("User@email.com");
        user5.setBirthday(LocalDate.of(2035, 12, 11));
        ValidationException thrown5 = assertThrows(
                ValidationException.class,
                () -> userController.create(user5),
                "Ожидалось исключение ValidationException для неверной даты рождения"
        );
        assertEquals("Дата рождения не может быть в будущем", thrown5.getMessage());
    }

    @Test
    void updateUserWithErrors() {
        User user = new User();
        user.setLogin("User");
        user.setName("Name");
        user.setEmail("User@email.com");
        user.setBirthday(LocalDate.of(2000, 12, 11));
        User addedUser = userController.create(user);
        User newUser = new User();
        newUser.setLogin("User");
        newUser.setName("Name");
        newUser.setEmail("User@email.com");
        newUser.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown1 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser),
                "Ожидалось исключение ValidationException для пустого ID"
        );
        assertEquals("Id пользователя должен быть указан", thrown1.getMessage());
        User newUser2 = new User();
        newUser2.setId(2);
        newUser2.setLogin("User");
        newUser2.setName("Name");
        newUser2.setEmail("User@email.com");
        newUser2.setBirthday(LocalDate.of(2000, 12, 11));
        NotFoundException thrown2 = assertThrows(
                NotFoundException.class,
                () -> userController.update(newUser2),
                "Ожидалось исключение NotFoundException для ненайденного пользователя"
        );
        assertEquals("Пользователь с id = " + newUser2.getId() + " не найден", thrown2.getMessage());
        User newUser3 = new User();
        newUser3.setId(1);
        newUser3.setLogin("");
        newUser3.setName("Name");
        newUser3.setEmail("User@email.com");
        newUser3.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown3 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser3),
                "Ожидалось исключение ValidationException для пустого логина"
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", thrown3.getMessage());
        User newUser4 = new User();
        newUser4.setId(1);
        newUser4.setLogin("Login log");
        newUser4.setName("Name");
        newUser4.setEmail("User@email.com");
        newUser4.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown4 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser4),
                "Ожидалось исключение ValidationException для логина с пробелом"
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", thrown4.getMessage());
        User newUser5 = new User();
        newUser5.setId(1);
        newUser5.setLogin("Login log");
        newUser5.setName("Name");
        newUser5.setEmail("");
        newUser5.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown5 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser5),
                "Ожидалось исключение ValidationException для неверной почты"
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown5.getMessage());
        User newUser6 = new User();
        newUser6.setId(1);
        newUser6.setLogin("Login log");
        newUser6.setName("Name");
        newUser6.setEmail("Email.com");
        newUser6.setBirthday(LocalDate.of(2000, 12, 11));
        ValidationException thrown6 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser6),
                "Ожидалось исключение ValidationException для неверной почты"
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown6.getMessage());
        User newUser7 = new User();
        newUser7.setId(1);
        newUser7.setLogin("Login");
        newUser7.setName("Name");
        newUser7.setEmail("User@email.com");
        newUser7.setBirthday(LocalDate.of(2035, 12, 11));
        ValidationException thrown7 = assertThrows(
                ValidationException.class,
                () -> userController.update(newUser7),
                "Ожидалось исключение ValidationException для неверной даты рождения"
        );
        assertEquals("Дата рождения не может быть в будущем", thrown7.getMessage());
    }
}