package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Пришёл запрос на добавление пользователя с ником '{}'", user.getLogin());
        validate(user);
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Пользователь с ником '{}' успешно добавлен.", user.getLogin());
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        log.info("Пришёл запрос на обновление данных пользователя с ником '{}'", newUser.getLogin());
        validate(newUser);
        if (newUser.getId() == null) {
            throw new RuntimeException("ID должен быть указан.");
        }
        log.info("Ищем пользователя с ID '{}'", newUser.getId());
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.info("Найден пользователь с ID '{}'", newUser.getId());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Обновлены данные пользователя с ID '{}'", newUser.getId());
            return oldUser;
        }
        throw new RuntimeException("Пользователь с ID " + newUser.getId() + " не найден.");
    }

    protected void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new RuntimeException("Электронная почта должна быть задана и содержать символ '@'.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new RuntimeException("Логин не может быть пустым и не должен содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Пользователь не задал имя, посему будет отображаться его логин.");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException("Дата рождения не может быть будущей.");
        }

    }
}
