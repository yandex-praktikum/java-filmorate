package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import static ru.yandex.practicum.filmorate.validation.Validation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController{
    private static final List<User> users = new ArrayList<>();
    private int userId = 1;

    @GetMapping
    public List<User> allUsers(){
        log.info("Получен GET-запрос на all");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        log.info("Получен POST-запрос с объектом User: {}", user);
        user.setId(userId);
        userValidate(user);
        users.add(user);
        log.info("Пользователь {} c id={} добавлен", user.getName(), userId);
        increaseUserId();
        return user;
    }

    @PutMapping
    public static User updateUser(@Valid @RequestBody User user){
        log.info("Получен PUT-запрос с объектом User: {}", user);
        userValidate(user);
        int userId = user.getId();
        for (User u : users) {
            if (u.getId() == userId) {
                users.set(users.indexOf(u), user);
                log.info("Пользователь c id={} обновлён", userId);
                return user;
            }
        }
        throw new ValidationException("Пользователь с id=" + userId + " не найден");
    }

    private void increaseUserId(){
        userId++;
    }
}
