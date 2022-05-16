package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET-запрос к эндпоинту /users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        if ((user.getName() == null || user.getName().equals(""))) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            assignId(user);
        } else {
            throw new ValidationException("Пользователь должен быть передан без id");
        }
        users.put(user.getId(), user);
        log.info("Создан объект '{}'", user);
        return user;
    }

    @PutMapping
    public User update(@Validated @RequestBody User user){
        if(user.getId() == 0){
            throw new ValidationException("Введите id пользователя, которого необходимо обновить");
        }
        users.put(user.getId(), user);
        log.info("Обновлен объект '{}'", user);
        return user;
    }

    private void assignId(User user) {
        user.setId(id);
        id++;
    }
}
