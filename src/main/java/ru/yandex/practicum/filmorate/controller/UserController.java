package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        try {
            checkUser(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Создан новый пользователь с id: {}", user.getId());
            return user;
        } catch (ValidationException e) {
            log.error("Невозможно создать пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User update(@RequestBody User user) {
        User existingUser;
        if (user.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Должен быть указан id");
        }
        if(users.containsKey(user.getId())) {
            try {
                checkUser(user);
                existingUser = users.get(user.getId());
                existingUser.setBirthday(user.getBirthday());
                existingUser.setLogin(user.getLogin());
                existingUser.setName(user.getName());
                existingUser.setEmail(user.getEmail());
                log.info("Обновлен пользователь с id: {}", user.getId());
            } catch (ValidationException e) {
                log.error("Невозможно обновить пользователя: {}", e.getMessage());
                throw e;
            }
        } else {
            log.error("Существует пользователь со следующим id: {}", user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
        return existingUser;
    }

    private void checkUser(User user) {

        if(user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if(user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
