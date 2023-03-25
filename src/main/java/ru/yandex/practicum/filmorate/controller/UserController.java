package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {
    static private int userId;
    private final Map<Integer, User> userMap;

    public UserController() {
        userMap = new HashMap<>();
        userId = 0;
    }

    public Integer generateId() {
        return ++userId;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        for (User valueComparison : userMap.values()) {
            if (valueComparison.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Фильм уже есть в нашей базе");
            }
        }
        user.setId(generateId());
        if (user.getLogin().contains(" ")) {
            log.warn(user.getLogin());
            throw new RuntimeException("Логин не может быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.trace("Добавлен пользователь {} ", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            userMap.put(user.getId(), user);
            log.trace("Обновлен узер: {}", user);
            return user;
        } else {
            throw new ValidationException("В базе нет такого пользователя");
        }
    }

    @GetMapping
    public List<User> userList() {
        return new ArrayList<>(userMap.values());
    }
}

