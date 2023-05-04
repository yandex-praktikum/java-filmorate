package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotExistException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 0;

    /////////////////////////////// Валидаторы ///////////////////////////////

    private static void validateLogin(String login) {
        int spacePosition = login.indexOf(' ');
        if (spacePosition > -1) {
            throw new ValidateException("Логин не должен содержать пробелы: " + login);
        }
    }

    ///////////////////////////// Обработчики REST ///////////////////////////

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateLogin(user.getLogin());
        if (users.containsKey(user.getId())) {
            throw new ObjectAlreadyExistException("User",user.getId());
        }
        user.setId(++currentId);
        String name = user.getName();
        if ((name == null) || (name.isBlank())) {
            user.setName(user.getLogin());
        }
        users.put(currentId,user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateLogin(user.getLogin());
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotExistException("User",user.getId());
        }
        users.put(user.getId(),user);
        return user;
    }
}
