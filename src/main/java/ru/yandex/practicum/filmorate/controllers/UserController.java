package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> getUsers() {
        log.info("Получен запрос, количество пользователей: " + users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        user.generateId();
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.debug("Добавлен пользователь: " + user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User pusUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Обновлены данные пользователя: " + user);
            users.put(user.getId(), user);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Пользователь не найден");

        }
        return user;
    }
}
