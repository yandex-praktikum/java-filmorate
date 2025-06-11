package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exeptions.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.exeptions.ValidateLoginIncorrectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    public final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Getting users...");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        log.info("Start addition of users...");

        if (user.getLogin().contains(" ")) {
            log.warn("Warning: Incorrect user login {}", user.getLogin());
            throw new ValidateLoginIncorrectException("User email can not contains white space symbols");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNexId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.trace("Вызван метод updateUser");
        User user = users.get(newUser.getId());

        if (user == null) {
            log.error("Error: user not found");
            throw new ElementNotFoundException("User not found");
        }

        users.put(newUser.getId(), newUser);
        return newUser;
    }


    private Long getNexId() {
        long currentMaxId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        log.debug("currentUserMaxId {}", currentMaxId);
        return ++currentMaxId;
    }
}