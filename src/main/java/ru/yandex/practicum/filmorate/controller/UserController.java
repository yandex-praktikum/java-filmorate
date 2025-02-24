package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();


    public @GetMapping Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null ||user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        log.info("Пользователь {} успешно создан", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {

        long id = user.getId();

        if (id == 0) {
            log.error("Ошибка при обновлении пользователя: ID не может быть равен 0");
            throw new ConditionsNotMetException("ID не может быть равен 0");
        }

        if (!users.containsKey(id)) {
            log.error("Ошибка при обновлении пользователя: Пользователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь не найден.");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(id, user);
        log.info("Пользователь успешно обновлен: {}", user);
        return user;
    }


    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

