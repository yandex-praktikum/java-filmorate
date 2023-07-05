package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static int userId = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен список всех пользователей.");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUserData(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Зарегистрирован новый пользователь: " + user.getName());

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validateUserData(user);
            users.put(user.getId(), user);
            log.info("Обновлена информация пользователя " + user.getName());
        } else {
            throw new UserValidationException("Пользователь не найден.");
        }

        return user;
    }

    public void validateUserData(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Заменяем пустое имя на логин.");
            user.setName(user.getLogin());
        }

        if (user.getLogin().contains(" ")) {
            throw new UserValidationException("Логин не может содержать пробелы.");
        }
    }
}