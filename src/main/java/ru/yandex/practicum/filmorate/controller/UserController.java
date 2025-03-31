package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private int userIdCounter = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        correctDataOfUser(user);
        user.setId(++userIdCounter);
        users.put(user.getId(), user);
        log.info("Новый пользователь успешно добавлен: ID = {}, Логин = {}, Имя = {}, Почта = {}, День рождения = {}",
                user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("Не указан Id пользователя");
            throw new ValidationException("Id пользователя должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.info("Пользователь для обновления данных найден: ID = {}, Логин = {}, Имя = {}, Почта = {}, День рождения = {}",
                    oldUser.getId(), oldUser.getLogin(), oldUser.getName(), oldUser.getEmail(), oldUser.getBirthday());
            correctDataOfUser(newUser);
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Данные пользователя обновлены: ID = {}, Логин = {}, Имя = {}, Почта = {}, День рождения = {}",
                    oldUser.getId(), oldUser.getLogin(), oldUser.getName(), oldUser.getEmail(), oldUser.getBirthday());
            return oldUser;
        }
        log.warn("Ошибка при обновлении пользователя: пользователь с ID {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private void correctDataOfUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Ошибка при добавлении пользователя: поле электронной почты пустое и(или) не содержит символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Ошибка при добавлении пользователя: логин пустой и(или) содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка при добавлении пользователя: дата рождения указана в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
