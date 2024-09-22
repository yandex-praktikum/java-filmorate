package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (UserValidator.isValidate(user)) {
            user.setId(getNextId());
            users.put(user.getId(), user);
        } else
            throw new ValidationException("Непредвиденная ошибка..");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (UserValidator.isValidate(user)) {
            if (users.containsKey(user.getId()))
                users.replace(user.getId(), user);
            else throw new ValidationException("Пользователя с таким id не существует");
        } else
            throw new ValidationException("Непредвиденная ошибка..");
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    private Integer getNextId() {
        Integer currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}