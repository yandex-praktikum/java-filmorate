package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @ResponseBody
    @GetMapping
    public Collection<User> getUsers() {
        log.info("The number of users is '{}'", users.size());
        return users.values();
    }

    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (!user.getEmail().contains("@") || user.getBirthday().isAfter(LocalDate.now())
                || user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new ValidationException("Incorrect data");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            if (user.getId() == 0) {
                user.setId(1);
            }
            users.put(user.getId(), user);
            log.info("The user '{}' is saved with the identifier '{}'.", user.getEmail(), user.getId());
        }
        return user;
    }

    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getLogin().isEmpty()
                || user.getLogin().isBlank() || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Incorrect data");
        }
        users.put(user.getId(), user);
        log.info("'{}' info with id '{}' was updated", user.getLogin(), user.getId());
        return user;
    }
}
