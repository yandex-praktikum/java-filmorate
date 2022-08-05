package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @GetMapping
    public List<User> gettingAllUsers() {
        log.debug("Получен запрос GET /users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        createId(user);
        isValid(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Получен запрос POST. Передан обьект {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        isValid(user);
        if (users.containsKey(user.getId())) {
            if (!users.containsValue(user)) {
                users.replace(user.getId(), user);
                log.debug("Пользователь обновлен");
            } else {
                log.debug("Пользователь уже создан");
            }
            return user;
        } else {
            log.error("Передан запрос PUT с некорректным данными пользователя{}", user);
            throw new ValidationException(HttpStatus.resolve(500));
        }

    }

    private void isValid(User user) {
        if(user.getLogin().contains(" ") || user.getId()<1 || user.getLogin().isBlank() ||
        user.getBirthday().isAfter(LocalDate.now()) || user.getEmail().isBlank() || !user.getEmail().contains("@")){
            throw new ValidationException(HttpStatus.resolve(500));
        }
    }

    private void createId(User user) {
        id++;
        user.setId(id);
    }

}
