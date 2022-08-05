package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> gettingAllUsers() {
        log.debug("Получен запрос GET /users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (isValid(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.debug("Получен запрос POST. Передан обьект {}", user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("Передан запрос POST с некорректным данными.");
            throw new ValidationException("Ошибка валидации пользователя!");
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (isValid(user) && users.containsKey(user.getId())) {
            if (!users.containsValue(user)) {
                    users.replace(user.getId(), user);
            } else {
                log.debug("Пользователь уже создан");
            }
            return user;
        } else {
            log.error("Передан запрос POST с некорректным данными пользователя.");
            throw new ValidationException("Ошибка валидации пользователя!");
        }

    }

    private boolean isValid(User user) {
        return !user.getLogin().contains(" ");
    }


}
