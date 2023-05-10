package ru.yandex.practicum.filmorate.model.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.service.IdCounter;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> getUsers() { // получение всех фильмов.
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) { // добавление фильма

        if (checkUserCorrection(user)) {
            user.setId(IdCounter.createUserId());
            users.add(user);

        }

        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        User result = null;
        if (checkUserCorrection(user)) {
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    u.setEmail(user.getEmail());
                    u.setLogin(user.getLogin());
                    u.setName(user.getName());
                    u.setBirthday(user.getBirthday());
                } else {
                    throw new ValidationException("Некорректный Id: " + user.getId());
                }
            }

            for (User user1 : users) {
                if (user1.equals(user)) {
                    result = user1;
                }
            }
        }
        return result;
    }

    private boolean checkUserCorrection(User user) {

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("У пользователя должен быть логин: " + user.getLogin());

        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("У пользователя некорректный емейл: " + user.getEmail());

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения пользователя: " + user.getBirthday());

        } else if (user.isEmptyName()) {
            user.setName(user.getLogin());
        }

        return true;
    }

}
