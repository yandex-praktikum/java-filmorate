package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer,User> users = new HashMap<>();
    private int i = 0;

    public User getUser(int id){
        return users.get(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        LocalDate currentMoment = LocalDate.now();
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!user.getEmail().contains("@") || user.getEmail().isEmpty() || user.getLogin().isEmpty() && user.getLogin().contains(" ") || user.getBirthday().isAfter(currentMoment)) {
            log.debug("Валидация не пройдена: {}", user);
            throw new ValidationException();
        }
        user.setId(i = i + 1);
        log.debug("Сохраняем нового пользователя: {}", user);
        users.put(i, user);
        return user;
    }

    @PutMapping
    public User saveUser(@RequestBody User user) throws ValidationException {
        if (user.getId() < 0) {
            log.debug("Валидация не пройдена: {}", user);
            throw new ValidationException();
        }
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                log.debug("Обновляем данные пользователя: {}", oldUser);
                return oldUser;
            }
        }
        log.debug("Добовляем пользователя: {}", user);
        return user;
    }
}

