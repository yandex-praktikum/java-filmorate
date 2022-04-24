package ru.yandex.practicum.controllers;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Data
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<String, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, вместо имени будет использован логин.");
        }

        user.setId(id);
        id++;
        users.put(user.getEmail(), user);
        log.info("Создан пользователь {}", user, toString());
        return user;
    }

    @PutMapping
    public User change(@Valid @RequestBody User user) {

        if (user.getName() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        users.put(user.getEmail(), user);
        log.info("Пользователь " + user.getEmail() + " изменен.");

        return user;
    }

}
