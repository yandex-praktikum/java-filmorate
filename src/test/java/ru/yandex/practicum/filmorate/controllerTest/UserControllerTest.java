package ru.yandex.practicum.filmorate.controllerTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @SneakyThrows
    @Test
    void addUser() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        final Map<Long, User> users = userController.findAllForTest();

        assertNotNull(users, "Список не возвращается.");
        assertEquals(1, users.size(), "Неверное количество в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void addUserWithIdAlreadyExist() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();

        User user2 = new User("login", "name", userId, "ya@ya.ru", LocalDate.of(1900, 01, 01));

        final IdAlreadyExistException exception = assertThrows(
                IdAlreadyExistException.class,
                () -> userController.create(user2));

        assertEquals("Ошибка IdAlreadyExistException: такой id уже существует", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void addUserWithLoginIsNull() {
        User user1 = new User(null, "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> userController.create(user1));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, User> Users = userController.findAllForTest();
        assertEquals(0, Users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void addUserWithLoginIsBlank() {
        User user1 = new User(" ", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> userController.create(user1));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(0, users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void addUserWithoutName() {

        User user1 = new User("login", null, null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();

        final Map<Long, User> users = userController.findAllForTest();

        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals("login", users.get(userId).getName(), "Имя не совпадает");
    }

    @Test
    void addUserWithEmailIsNull() {
        User user1 = new User("login", "name", null, null, LocalDate.of(1900, 01, 01));

        final InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userController.create(user1));

        assertEquals("Ошибка InvalidEmailException: некорректный email", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(0, users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void addUserWithInvalidEmail() {
        User user1 = new User("login", "name", null, "yaya.ru", LocalDate.of(1900, 01, 01));

        final InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userController.create(user1));

        assertEquals("Ошибка InvalidEmailException: некорректный email", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(0, users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void addUserWithBirthdateInFuture() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(2022, 10, 01));

        final InvalidBirthdateException exception = assertThrows(
                InvalidBirthdateException.class,
                () -> userController.create(user1));

        assertEquals("Ошибка InvalidBirthdateException: пользователь из будущего", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(0, users.size(), "Неверное количество объектов в списке");
    }

    @Test
    void updateUser() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("new login", "name", userId, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        userController.update(user2);

        final Map<Long, User> users = userController.findAllForTest();

        assertNotNull(users, "Список не возвращается.");
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user2, users.get(userId), "Объекты не совпадают");

    }

    @Test
    void updateUserWithIdIsNull() {

        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("new login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidIdException: некорректный или пустой id", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithNonExistentId() {

        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("new login", "name", 865232326L,"ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidIdException exception = assertThrows(
                InvalidIdException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidIdException: некорректный или пустой id", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithLoginIsNull() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User(null, "name", userId,"ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithLoginIsBlank() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User(" ", "name", userId,"ya@ya.ru", LocalDate.of(1900, 01, 01));

        final InvalidNameException exception = assertThrows(
                InvalidNameException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidNameException: отсутствует имя", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithoutName() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("login", null, userId,"ya@ya.ru", LocalDate.of(1900, 01, 01));
        userController.update(user2);

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals("login", users.get(userId).getName(), "Имя не совпадает");
    }

    @Test
    void updateUserWithEmailIsNull() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("login", "name", userId,null, LocalDate.of(1900, 01, 01));

        final InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidEmailException: некорректный email", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithInvalidEmail() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("login", "name", userId,"yaya.ru", LocalDate.of(1900, 01, 01));

        final InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidEmailException: некорректный email", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void updateUserWithBirthdateInFuture() {
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("login", "name", userId,"ya@ya.ru", LocalDate.of(2022, 11, 01));

        final InvalidBirthdateException exception = assertThrows(
                InvalidBirthdateException.class,
                () -> userController.update(user2));

        assertEquals("Ошибка InvalidBirthdateException: пользователь из будущего", exception.getMessage());

        final Map<Long, User> users = userController.findAllForTest();
        assertEquals(1, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
    }

    @Test
    void findAllUsers(){
        User user1 = new User("login", "name", null, "ya@ya.ru", LocalDate.of(1900, 01, 01));
        final Long userId = userController.create(user1).getId();
        user1.setId(userId);

        User user2 = new User("login", "name", null,"ya@ya.ru", LocalDate.of(2020, 11, 01));
        final Long userId2 = userController.create(user2).getId();
        user1.setId(userId2);

        final Map<Long, User> users = userController.findAllForTest();
        assertNotNull(users, "Список не возвращается.");
        assertEquals(2, users.size(), "Неверное количество объектов в списке");
        assertEquals(user1, users.get(userId), "Объекты не совпадают");
        assertEquals(user2, users.get(userId2), "Объекты не совпадают");
    }

}
