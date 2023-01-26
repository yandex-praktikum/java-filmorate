package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("добавление фильма");
        validateUser(user);

        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("обновление фильма");
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с таким id не существует");
            throw new ValidationException("Пользователь с таким id не существует");
        }

        users.replace(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("получение списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {
        if (user == null) {
            log.warn("Пользователь не может быть null");
            throw new ValidationException("Пользователь не может быть null");
        }
        if (!Validator.isEmail(user.getEmail())) {
            log.warn("некорректный email");
            throw new ValidationException("некорректный email");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("некорректный логин");
            throw new ValidationException("некорректный логин");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("запись в поле имени значение логина, если полученное имя пустое");
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

}
