package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idUser = 0;

    // получение списка пользователей
      
    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    // создание пользователя
      
    @PostMapping()
    public User addUser(@RequestBody User user) throws ValidationException {
        UserValidator.isValidUsers(user);
        int i = generateIdUsers();
        user.setId(i);
        log.debug("Сохранили: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    // обновление профиля пользователя
      
    @PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            UserValidator.isValidUsers(user);
            log.debug("Обновили: {}", user);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Такого пользователя нет в базе.");
        }
        return user;
    }

    // создание уникального id пользователя
    private int generateIdUsers() {
        return ++idUser;
    }
}