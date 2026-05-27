package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

// Класс контроллер пользователей
public class UserController {

    // Коллекция пользователей (ключ - id пользователя, значение - объект User)
    private final Map<Integer, User> users = new HashMap<>();
    // Счётчик для генерации уникальных id
    private int currentId = 0;

    // Обрабатывает POST-запросы на /users
    @PostMapping
    public User create(@RequestBody User user) { // Метод создания пользователя
        validate(user);
        fillName(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    // Обрабатывает PUT-запросы на /users
    @PutMapping
    public User update(@RequestBody User user) { // Метод обновления пользователя
        validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " не найден");
        }
        fillName(user);
        users.put(user.getId(), user);
        log.info("Обновлён пользователь: {}", user);
        return user;
    }

    // Обрабатывает GET-запросы на /users
    @GetMapping
    public Collection<User> findAll() { // Метод получения всех пользователей
        return new ArrayList<>(users.values());
    }

    // Метод генерации нового id
    private int getNextId() {
        return ++currentId;
    }

    // Метод проверки имени пользователя
    private void fillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    // Метод валидации пользователя
    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не передан");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не может быть пустым и должен содержать @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
