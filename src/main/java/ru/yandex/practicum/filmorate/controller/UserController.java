package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        LocalDate currentMoment = LocalDate.now();
        if (user.getEmail().contains("@") && user.getEmail().isEmpty() || user.getLogin().isEmpty() && user.getLogin().contains(" ") || user.getBirthday().isAfter(currentMoment)) {
            throw new ValidationException();
        }
        log.debug("Сохраняем пользователя: {}", user);
        users.add(user);
        return user;
    }

    @PutMapping
    public User saveUser(@RequestBody User user) throws ValidationException {
        for (User oldUser : users) {
            if (oldUser.getId() == user.getId()) {
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                return oldUser;
            }
        }
        return user;
    }
}

