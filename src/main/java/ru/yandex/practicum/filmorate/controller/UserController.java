package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Integer, User> users;
    int id;

    public UserController() {
        users = new HashMap<>();
        id = 0;
    }
    private int setId() {
        return ++id;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрошен текущий список пользователей: " + users);
        return users.values();
    }
    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        int userId = setId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Создан пользователь " + user);
        return user;
    }
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        checkUserInBase(user);
        validateUser(user);
        int userId = user.getId();
        User oldUser = users.get(userId);
        users.put(userId, user);
        log.info("Пользователь " + oldUser + " изменен на " + user);
        return user;
    }

    private void checkUserInBase(User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + "не найден.");
        }
    }

    /**
     * Для проверки на полный повтор всех полей.
     * Но с ней не проходит тест sprint.json.
     */
    private void findUser(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        for (User u: users.values()) {
            if (u.equals(user)) {
                throw new ValidationException("Такой пользователь уже имеется с id " + u.getId());
            }
        }
    }
    private void validateUser(User user) throws ValidationException {
        if (user.getBirthday() == null) {
            throw new ValidationException("Не задан день рождения.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем.");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
