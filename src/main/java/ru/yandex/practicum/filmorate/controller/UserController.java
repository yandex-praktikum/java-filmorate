package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> allListUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Начало создания пользователя");

        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.warn("Электронная почта пользователя {}", user.getEmail());
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            log.warn("Логин пользователя пуст");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().trim().isEmpty() || user.getName().isBlank()) {
            log.debug("Имя пользователя пустое, используется логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        User createUser = User.builder().id(generationId())
                .name(user.getName())
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();

        log.debug("Пользователь создан {}", createUser);
        users.put(createUser.getId(), createUser);
        log.debug("Пользователь добавлен в хранилище {}", createUser.getId());

        return createUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Начало обновления пользователя");

        if (newUser.getId() == null) {
            log.debug("Id не указан для обновления");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("Id пользователя не найден {}", newUser.getId());
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        User existing = users.get(newUser.getId());

        log.debug("Обновление пользователя с Id: {}", existing.getId());

        User updateUser = User.builder().id(existing.getId())
                .name(newUser.getName())
                .login(newUser.getLogin())
                .email(newUser.getEmail())
                .birthday(newUser.getBirthday())
                .build();

        users.put(updateUser.getId(), updateUser);
        log.info("Пользователь обновлён с Id: {}", updateUser.getId());
        return updateUser;
    }

    private long generationId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}