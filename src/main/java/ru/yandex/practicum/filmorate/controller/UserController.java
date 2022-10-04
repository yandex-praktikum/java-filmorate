package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id;

    public UserController() {
        id = 0;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Отаправлен перечень пользователей {}", users);
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь " + user + " уже существует");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        updateID();
        user.setId(id);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Невозможно обновить. Пользователь " + user + " не существует");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Обновлен пользователь {}", user);

        return user;
    }

    private void updateID() {
        id++;
    }
}
