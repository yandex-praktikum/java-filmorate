package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
UserServiceImpl  userService;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl();
    }

    @Test
    void updateUsers() {


    }

    @Test
    void addUsers() {
    }

    @Test
    void getAllUsers() {

        UserController controller = new UserController();
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1146, 8, 20));

        User user1 = userService.addUsers(user);

        assertEquals(user, user1, "Пользователи не совпадают.");

    }
}