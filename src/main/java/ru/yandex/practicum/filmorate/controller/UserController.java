package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.InMemoryUserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserService userService;

    public UserController(InMemoryUserService userDBService) {
        this.userService = userDBService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidateException {
        userService.createUser(user);
        log.info("добавлен user: {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidateException {
        userService.updateUser(user);
        log.info("обновлен user {}", user.getId());
        return user;
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        log.info("запрошен пользователь {}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("запрошены все пользователи");
        return userService.getAll();
    }

    public int getCountUsers() {
        return userService.getAll().size();
    }

    @DeleteMapping
    public void deleteUsers() {
        userService.deleteAll();
        log.info("Удалены все пользователи");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("Удален пользователь {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Добавлены в друзья к пользователю id={}, friendId={}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        log.info("Удалены из друзей пользователя id={}, friendId={}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug("Запрошен список друзей id={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("Запрошен список общих друзей id1={},id2={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
