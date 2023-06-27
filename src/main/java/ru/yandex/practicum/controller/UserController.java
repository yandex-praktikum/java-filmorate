package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exeptions.InvalidEmailException;
import ru.yandex.practicum.exeptions.UserAlreadyExistException;
import ru.yandex.practicum.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private Map<String, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping("/user")
    public User create(@RequestBody User user) throws UserAlreadyExistException, InvalidEmailException {

        if (users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException();

        }

        else if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new InvalidEmailException();
        }

        else {
            users.put(user.getEmail(), user);
            return user;
        }
    }

    @PutMapping("/user")
    public User update(@RequestBody User user) throws InvalidEmailException {
        if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new InvalidEmailException();
        }
        users.put(user.getEmail(), user);
        return user;
    }
}
