package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    UserIdGenerator userIdGenerator = new UserIdGenerator();
    UserController userController = new UserController(userIdGenerator);

    @Test
    void findAll() {
        User user = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        userController.create(user);
        assertEquals(1, userController.findAll().size(), "Коллекция пуста");
    }

    @Test
    void create() {
        User user = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        userController.create(user);
        User testUser = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        testUser.setId(user.getId());
        assertEquals(testUser, userController.getUser(user.getId()), "Пользователь не добавлен");
    }

    @Test
    void saveUser() {
        User user1 = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        userController.create(user1);
        User user2 = new User("yandex@mail.ru", "smozy", "Intel", LocalDate.of(2001, 5, 15));
        user2.setId(user1.getId());
        userController.saveUser(user2);
        User testUser = new User("yandex@mail.ru", "smozy", "Intel", LocalDate.of(2001, 5, 15));
        testUser.setId(user2.getId());
        assertEquals(testUser, userController.getUser(user2.getId()), "Пользователь не добавлен");
    }
}
