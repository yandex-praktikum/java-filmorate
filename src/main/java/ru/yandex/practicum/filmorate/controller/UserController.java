package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, HttpServletRequest request) {
        logRequest(request);
        if (isUserNotValid(user)) {
            log.debug("В запросе переданы некорректные данные для добавления пользователя.");
            throw new ValidationException("Некорректные данные для добавления пользователя.");
        } else {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            newId();
            user.setId(id);
            users.put(user.getId(), user);
            log.debug("Добавлен новый пользователь: " + user);
            return ResponseEntity.ok(user);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, HttpServletRequest request) {
        logRequest(request);
        if (isUserNotValid(user)) {
            log.debug("В запросе переданы некорректные данные для добавления пользователя.");
            throw new ValidationException("Некорректные данные для обновления пользователя.");
        } else if (!users.containsKey(user.getId())) {
            throw new ValidationException("Некорректный id для обновления пользователя.");
        } else {
            users.put(user.getId(), user);
            log.debug("Пользователь обновлен: " + user);
            return ResponseEntity.ok(user);
        }
    }


    private boolean isUserNotValid(User user) {
        return (user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now()));
    }

    private void newId() {
        ++id;
    }

    private void logRequest(HttpServletRequest request) {
        log.debug("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
    }
}