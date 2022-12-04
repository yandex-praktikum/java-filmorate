package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping("/users") // получение списка пользователей
    public List<User> findAll() {
        return userStorage.findAll();
    }


    @PostMapping(value = "/users") // добавление пользователя
    public User create(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.create(user);
    }

    @PutMapping(value = "/users") // обновление пользователя
    public User update(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.update(user);
    }

    @GetMapping("/users/{id}") // получение пользователя по ID
    public User findUser(@PathVariable int id) {
        return userService.findUser(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}") //добавление в друзья
    public void addToFriendList(@PathVariable int id, @PathVariable int friendId) {
        userService.addToFriendList(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}") //удаление из друзей
    public void removeFromFriendList(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFromFriendList(id, friendId);
    }

    @GetMapping("/users/{id}/friends") // получение друзей указанного пользователя
    public List<User> getFriendList(@PathVariable int id) {

        return userService.getFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}") // получение общих друзей
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
