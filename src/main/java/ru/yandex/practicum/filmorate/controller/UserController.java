package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static int id = 0;
    private final Map<Integer, User> users;

    public UserController() {
        users = new HashMap<>();
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Current number of users: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user)  {
            validateUser(user);
            user.setId(++id);
            users.put(user.getId(), user);
            log.debug("User created: " + user);
        return user;
    }

    @PutMapping
    public User update (@RequestBody User user)  {
        if (!(users.containsKey(user.getId()))) {
            throw new ValidationException("User with id " + user.getId() +" not found");
        }
        try {
            validateUser(user);
            users.put(user.getId(), user);
            log.debug("User updated: " + user);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return user;
    }

        public void validateUser(User user) {
            if (!(user.getEmail().contains("@"))) {
                throw new ValidationException("A valid e-mail should contain @");
            }
            if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
                throw new ValidationException("Invalid login, no spaces allowed");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Birthday should be in the past");
            }
        }
}


