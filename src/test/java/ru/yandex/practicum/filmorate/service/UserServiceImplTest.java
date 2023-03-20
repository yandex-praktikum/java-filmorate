package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    private UserServiceImpl  userService;
    User user;
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
        user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
    }

    @Test
    void addUsers() {

        user.setBirthday(LocalDate.of(2046, 8, 20));
        ConstraintViolationException e = assertThrows(ConstraintViolationException.class,
                () -> userService.addUsers(user));
        assertEquals("The user with the specified id was not found.", e.getMessage());
        assertEquals(0, userService.getAllUsers().size(),
                "The number of users does not match the expected.");

    }
}