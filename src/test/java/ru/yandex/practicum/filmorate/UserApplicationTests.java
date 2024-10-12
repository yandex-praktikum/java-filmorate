package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class UserApplicationTests {

    private UserController userController;
    private static final LocalDate birthday = LocalDate.of(2001, 10, 9);

    @BeforeEach
    public void setUp() {
        this.userController = new UserController();
    }

    @Test
    public void shouldCreateUser() {
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("Daniel")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        User userControllerCreate = userController.create(user);

        Assertions.assertEquals(user, userControllerCreate, "Пользователи должны быть одинаковы");
    }

    @Test
    public void shouldUpdateUser() {
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("Daniel")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        userController.create(user);

        user = User.builder()
                .id(1L)
                .name("Smolllll")
                .login("Daniel")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        User userControllerUpdate = userController.update(user);

        Assertions.assertEquals(user, userControllerUpdate, "Пользователи должны быть одинаковы");
    }

    @Test
    public void shouldGetAllUsers() {
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("Daniel")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        userController.create(user);

        User user2 = User.builder()
                .id(2L)
                .name("AntonCa")
                .login("Anton")
                .email("anon.anton@yandex.ru")
                .birthday(birthday)
                .build();

        userController.create(user2);

        List<User> usersList = userController.allListUsers();

        Assertions.assertEquals(2, usersList.size(), "Должно быть два пользователя");
    }

    @Test
    public void shouldntBirthdayToBeInAfter() {
        LocalDate localDate = LocalDate.of(2025, 11, 12);
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("Daniel")
                .email("dany.smol@yandex.ru")
                .birthday(localDate)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    public void shouldntEmailToBeEmptyAndNoHaveChar() {
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("Daniel")
                .email("dany.smolyandex.ru")
                .birthday(birthday)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    public void shouldntLoginToBeEmptyAndHaveSpaces() {
        User user = User.builder()
                .id(1L)
                .name("Smolcap")
                .login("")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
    }

    @Test
    public void shouldNameToBeEmptyButUseLogin() {
        User user = User.builder()
                .id(1L)
                .name(" ")
                .login("daniel")
                .email("dany.smol@yandex.ru")
                .birthday(birthday)
                .build();

        User create = userController.create(user);
        Assertions.assertEquals("daniel", create.getName(), "Если имя пустое в таком случае будет " +
                "использован логин");
    }
}
