package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.model.User;
import ru.yandex.service.UserServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userServiceImpl.createUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userServiceImpl.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userServiceImpl.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<Long> allFriends(@PathVariable Long id) {
        return userServiceImpl.allFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<Long> generalFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userServiceImpl.generalFriends(id, otherId);
    }

    @GetMapping("/users/{id}")
    public User getUserId(@PathVariable Long id) {
        return userServiceImpl.getUserId(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        return userServiceImpl.updateUser(newUser);
    }

    @GetMapping
    public Collection<User> allUsers() {
        return userServiceImpl.allUsers();
    }

    @DeleteMapping
    public User deleteUser(@RequestBody User user) {
        return userServiceImpl.deleteUser(user);
    }
}
