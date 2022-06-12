package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final UserIdGenerator userIdGenerator;

    @Autowired
    public UserController(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
    }

    public User getUser(int id) {
        return users.get(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        validation(user);
        int newId = userIdGenerator.generate();
        user.setId(newId);
        log.info("Сохраняем нового пользователя: {}", user);
        users.put(newId, user);
        return user;
    }

    @PutMapping
    public User saveUser(@RequestBody User user) {
        if (user.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", user.getId());
            throw new ValidationException();
        }
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                log.info("Обновляем данные пользователя: {}", oldUser);
                return oldUser;
            }
        }
        log.info("Добовляем пользователя: {}", user);
        return user;
    }

    private void validation(User user) {
        LocalDate currentMoment = LocalDate.now();

        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            log.error("Ошибка, валидация не пройдена. Электронная почта не может быть пустой и должна содержать символ @: {}", user.getEmail());
            throw new ValidationException();
        }
        if (user.getLogin().isEmpty() && user.getLogin().contains(" ")) {
            log.error("Ошибка, валидация не пройдена. Логин не может быть пустым и содержать пробелы: {}", user.getLogin());
            throw new ValidationException();
        }
        if (user.getBirthday().isAfter(currentMoment)) {
            log.error("Ошибка, валидация не пройдена. Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException();
        }
    }
}
