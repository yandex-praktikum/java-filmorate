package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    Map<Long, User> users = new HashMap<>();
    private long nextInt = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос списка всех пользователей. Текущее количество: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("Попытка добавить пользователя: {}", user);
        try {
            validateUserFields(user);
            validateUnique(user);

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            user.setId(genNextId());
            users.put(user.getId(), user);

            log.info("Пользователь добавлен успешно. ID: {}, Название: {}", user.getId(), user.getName());
            return user;
        } catch (ConditionsNotMetException | DuplicatedDataException e) {
            log.warn("Ошибка при добавлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Попытка внести обновления: {}", user);
        try {
            if (user.getId() == null) {
                throw new ConditionsNotMetException("Id должен быть указан");
            }
            User existingUser = users.get(user.getId());
            if (existingUser == null) {
                throw new ConditionsNotMetException("Пользователь не найден");
            }

            validateUserFields(user);
            validateUpdateUnique(user, existingUser);

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь обновлён. ID: {}, Новое имя: {}", existingUser.getId(), existingUser.getName());
            return user;
        } catch (ConditionsNotMetException | DuplicatedDataException e) {
            log.warn("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    private long genNextId() {
        return nextInt++;
    }

    private void validateUserFields(User user) {
        log.debug("Начало валидации {}", user);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Email не введен");
            throw new ConditionsNotMetException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.error("В Email нет @ ");
            throw new ConditionsNotMetException("Email должен содержать @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Пустой логин");
            throw new ConditionsNotMetException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы");
            throw new ConditionsNotMetException("Логин не может содержать пробелы");
        }
        if (user.getBirthday() == null) {
            log.error("Нет информации о дне рождении");
            throw new ConditionsNotMetException("Дата рождения не может быть пустой");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения в будущем");
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        log.debug("Валидация пройдена {}", user);
    }

    private void validateUnique(User user) {
        users.values().stream()
                .filter(u -> user.getEmail().equals(u.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new DuplicatedDataException("Такой email уже используется");
                });

        users.values().stream()
                .filter(u -> user.getLogin().equals(u.getLogin()))
                .findFirst()
                .ifPresent(u -> {
                    throw new DuplicatedDataException("Такой логин уже используется");
                });
    }

    private void validateUpdateUnique(User user, User existingUser) {
        if (!user.getEmail().equals(existingUser.getEmail())) {
            users.values().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .filter(u -> user.getEmail().equals(u.getEmail()))
                    .findFirst()
                    .ifPresent(u -> {
                        throw new DuplicatedDataException("Такой email уже используется");
                    });
        }

        if (!user.getLogin().equals(existingUser.getLogin())) {
            users.values().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .filter(u -> user.getLogin().equals(u.getLogin()))
                    .findFirst()
                    .ifPresent(u -> {
                        throw new ConditionsNotMetException("Такой логин уже используется");
                    });
        }
    }

}
