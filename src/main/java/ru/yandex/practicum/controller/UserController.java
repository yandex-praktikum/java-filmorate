package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер пользователей
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserStorage storage;
    private final UserService service;

    /**
     * Получение всех пользователей
     */
    @GetMapping("/users")
    public List<User> findAll() {
        return storage.findAll();
    }

    /**
     * Создание пользователя
     */
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        return storage.create(user);
    }

    /**
     * Обновление пользователя
     */
    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    /**
     * Получение пользователя по id
     */
    @GetMapping("/users/{id}")
    public User findUser(@PathVariable("id") Integer id) {
        return storage.findUserById(id);
    }

    /**
     * Добавление в друзья
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return service.addFriend(storage.findUserById(id), storage.findUserById(friendId));
    }

    /**
     * Удаление из друзей
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return service.deleteFriend(storage.findUserById(id), storage.findUserById(friendId));
    }

    /**
     * Получение друзей пользователя
     */
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return service.getFriends(storage.findUserById(id));
    }

    /**
     * Получение общих друзей
     */
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        List<Integer> commonIds = service.getCommonFriends(storage.findUserById(id),
                storage.findUserById(otherId));
        List<User> commonFriends = new ArrayList<>();
        for (Integer i : commonIds) {
            commonFriends.add(storage.findUserById(i));
        }
        return commonFriends;
    }
}