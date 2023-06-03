package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.Validator.validate;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int userId = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> userList() {
        log.debug("В списке " + users.size() + " пользователей");
        return new ArrayList<>(users.values());

    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())){
            log.debug("Пользователь уже существует - " + users.get(user.getId()));
            throw new ValidationException("Пользователь уже существует");
        } else {
            validate(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            userId++;
            user.setId(userId);
            users.put(user.getId(), user);
            log.debug("Пользователь добавлен " + user);
            return user;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.debug("Пользователь обновлен" + user);
            return user;
        }else {
            log.debug("Id отсутствует в списке - " + user.getId());
            throw new ValidationException("Пользователя с ID " + user.getId() + " нет в системе");
        }
    }


}
