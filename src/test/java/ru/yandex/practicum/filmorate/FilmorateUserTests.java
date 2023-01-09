package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateUserTests {

    @Autowired
    private UserController userController;

    @BeforeEach
    public void clearDB() {
        //userController.deleteUsers();
    }


    @Test
    public void userControllerValidEntityTest() throws ValidateException {
        User user = new User(1L, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        userController.deleteUsers();
        userController.createUser(user);
        Assertions.assertEquals(userController.getCountUsers(), 1, "ожидается - добавлен 1 пользователь");
    }

    @Test
    void userControllerInvalidEmailTest() {

        User user1 = new User(1L, "user1.example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));


        assertThrows(RuntimeException.class, () -> userController.createUser(user1));

    }

    @Test
    public void userControllerInvalidLoginTest() {
        User user = new User(2L, "user2@example", "User2"
                , "", LocalDate.of(2000, 1, 1));


        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        user.setLogin(null);
        assertThrows(NullPointerException.class, () -> userController.createUser(user));
        user.setLogin("user 2");
        assertThrows(RuntimeException.class, () -> userController.createUser(user));

    }

    @Test
    public void userControllerInvalidDateBirthTest() {
        User user = new User(2L, "user2@example", "User2"
                , "user2", LocalDate.of(2030, 1, 1));
        user.setId(2L);
        user.setBirthday(LocalDate.of(2030, 1, 1));
        user.setName("User2");
        user.setLogin("user2");
        user.setEmail("user2@example");

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
    }

    @Test
    public void userControllerUpdateTest() throws ValidateException {
        User user = new User(1L, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        User userUpd = new User(1L, "userNew@example", "UserNew"
                , "userNew", LocalDate.of(2002, 1, 1));

        userController.deleteUsers();
        userController.createUser(user);
        userController.updateUser(userUpd);
        Assertions.assertEquals(userController.getCountUsers(), 1, "ожидается - обновлен 1 пользователь");
    }

    @Test
    public void userControllerGetTest() throws IOException, InterruptedException, ValidateException {
        User user = new User(1L, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        User user2 = new User(2L, "userNew@example", "UserNew"
                , "userNew", LocalDate.of(2002, 1, 1));

        userController.deleteUsers();
        userController.createUser(user);
        userController.createUser(user2);
        Assertions.assertEquals(userController.getUsers().size(), 2
                , "ожидается - получили 2 пользователя");
    }


}
