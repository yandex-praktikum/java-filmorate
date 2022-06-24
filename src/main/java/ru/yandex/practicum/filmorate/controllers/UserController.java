package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Выполнен запрос getAllUsers.");
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = service.getUser(id);
        if (user == null)
            throw new UserNotFoundException();
        log.info("Выполнен запрос getUser.");
        return user;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (user.getId() != null)
            throw new UserValidationException("id");

        User userForSave = service.create(user);
        log.info("Выполнен запрос createUser. Текущее количество пользователей: " + service.getAllUsers().size());
        return userForSave;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (user.getId() == null)
            throw new UserValidationException("id");

        if (service.getUser(user.getId()) == null)
            throw new UserNotFoundException();

        User userForSave = service.update(user);
        log.info("Выполнен запрос updateUser.");
        return userForSave;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (service.getUser(id) == null || service.getUser(friendId) == null)
            throw new UserNotFoundException();

        service.addFriend(id, friendId);
        log.info("Выполнен запрос addFriend.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (service.getUser(id) == null || service.getUser(friendId) == null)
            throw new UserNotFoundException();

        service.removeFriend(id, friendId);
        log.info("Выполнен запрос removeFriend.");
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        if (service.getUser(id) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос getFriends.");
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (service.getUser(id) == null || service.getUser(otherId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос getCommonFriends.");
        return service.getCommonFriends(id, otherId);
    }
}