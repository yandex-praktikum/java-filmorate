package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int currentId = 1;
    private final List<User> users = new ArrayList<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            validateUser(user);
            user.setId(currentId++);
            users.add(user);
            log.info("Пользователь создан: {}", user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@RequestBody User updatedUser) {
        try {
            validateUser(updatedUser);
            for (User user : users) {
                if (user.getId() == updatedUser.getId()) {
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("Пользователь обновлён: {}", user);

                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }
            log.warn("Пользователь с ID {} не найден для обновления.", updatedUser.getId());
            return new ResponseEntity<>(new ErrorResponse("Пользователь не найден"), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Ошибка валидации: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}