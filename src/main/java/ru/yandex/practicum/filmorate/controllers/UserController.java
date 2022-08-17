package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private List<User> users = new ArrayList<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        users.add(user);
        log.info("add user");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.add(user);
        log.info("update user");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
