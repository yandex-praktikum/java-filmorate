package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    private int generatorId() {
        return userId++;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей: {}", users.size());
        return List.copyOf(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Попытка добавить пользователя: {}", user);

        validateUsers(user);
        user.setId(generatorId());
        users.put(user.getId(), user);
        log.debug("Пользователь успешно добавлен: {}", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Попытка обновить пользователя: {}", user);

        validateUsers(user);
        if (users.get(user.getId()) != null) {
            users.replace(user.getId(), user);
            log.debug("Пользователь успешно обновлен: {}", user);
        } else {
            log.debug("Такого пользователя не существует");
            throw new ValidationException("Такого пользователя не существует");
        }
        return user;
    }

    public User validateUsers(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Установлен логин вместо имени пользователя");
            user.setName(user.getLogin());
        }
        return user;
    }
}