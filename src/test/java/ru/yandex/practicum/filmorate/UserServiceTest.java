package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  User user = new User(1, "Name", LocalDate.of(1990, 10, 11),
            "fkg@mail.ru", "Login");
    User user1 = new User(1, "Name2", LocalDate.of(1990, 10, 11),
            "fsfg@mail.ru", "Login2");
    User user2 = new User(1, "Name3", LocalDate.of(1995, 10, 11),
            "sfg@mail.ru", "Login3");
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);

    UserController userController = new UserController(userService, inMemoryUserStorage);

    @Test
    public void finUserTest() throws ValidationException {
        userController.create(user);
        userController.findUser(1);
        assertEquals(userController.findAll().get(0), user);

    }

    @Test
    public void shouldThrowExceptionWhenIdDoesntExistTest() throws ValidationException {
        userController.create(user);
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userController.findUser(999)
        );
        assertEquals("There no user with such id", exception.getMessage());
    }

    @Test
    public void getCommonFriendsEmptyTest() throws ValidationException {
        userController.create(user);
        userController.create(user1);

        assertTrue(userController.getCommonFriends(user.getId(), user1.getId()).isEmpty());
    }

    @Test
    public void addFriendTest() throws ValidationException {
        userController.create(user);
        userController.create(user1);
        userController.addToFriendList(user.getId(), user1.getId());
        assertEquals(1, userController.findUser(user.getId()).getFriends().size());
    }

    @Test
    public void emptyCommonFriendsListTest() throws ValidationException {
        userController.create(user);
        userController.create(user1);
        userController.addToFriendList(user.getId(), user1.getId());

        assertTrue(userController.getCommonFriends(user.getId(), user1.getId()).isEmpty());
    }

    @Test
    public void getFriendsListTest() throws ValidationException {
        userController.create(user);
        userController.create(user1);
        userController.create(user2);
        userController.addToFriendList(user.getId(), user1.getId());
        userController.addToFriendList(user.getId(), user2.getId());

        System.out.println(userController.getFriendList(user.getId()));
        assertEquals(2, userController.getFriendList(user.getId()).size());
    }

    @Test
    public void addFriend3to1Test() throws ValidationException {
        userController.create(user);
        userController.create(user1);
        userController.create(user2);

      //  assertEquals(userController.addToFriendList(user.getId(), user2.getId()), List.of(user2));
    }

    @Test
    public void addFriend32to1() throws ValidationException {
        userController.create(user);
        userController.create(user1);
        userController.create(user2);

        userController.addToFriendList(user.getId(), user1.getId());
        userController.addToFriendList(user.getId(), user2.getId());

        assertEquals(2, userController.findUser(user.getId()).getFriends().size());
    }

}
