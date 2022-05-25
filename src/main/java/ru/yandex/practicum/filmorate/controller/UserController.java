package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody(required = false) User user) {
        if (users.containsKey(user.getId())) {
            log.info("Ошибка добавления данных пользователя: " + user);
            return user;
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody(required = false) User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Ошибка добавления данных пользователя: " + user);
            return user;
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public ArrayList<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
