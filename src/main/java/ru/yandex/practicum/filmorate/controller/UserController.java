package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.IdGeneratorUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Component
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final IdGeneratorUser idGeneratorUser;

    @Autowired
    public UserController(IdGeneratorUser idGeneratorUser) {
        this.idGeneratorUser = idGeneratorUser;
    }

    public User getUser(int id) {
        return users.get(id);
    }

    @GetMapping
    public Collection<User> findAll() {

        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        validation(user);
        int newId = idGeneratorUser.generate();
        user.setId(newId);
        log.debug("Сохраняем нового пользователя: {}", user);
        users.put(newId, user);
        return user;
    }

    @PutMapping
    public User saveUser(@RequestBody User user) {

        if (user.getId() < 0) {
            log.debug("Id не может быть отрицательным: {}", user.getId());
            throw new ValidationException();
        }
        for (User oldUser : users.values()) {
            if (oldUser.getId() == user.getId()) {
                oldUser.setEmail(user.getEmail());
                oldUser.setLogin(user.getLogin());
                oldUser.setName(user.getName());
                oldUser.setBirthday(user.getBirthday());
                log.debug("Обновляем данные пользователя: {}", oldUser);
                return oldUser;
            }
        }
        log.debug("Добовляем пользователя: {}", user);
        return user;
    }

    private void validation(User user) {

        LocalDate currentMoment = LocalDate.now();

        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            log.debug("Электронная почта не может быть пустой и должна содержать символ @: {}", user.getEmail());
            throw new ValidationException();
        }
        if(user.getLogin().isEmpty() && user.getLogin().contains(" ")){
            log.debug("Логин не может быть пустым и содержать пробелы: {}", user.getLogin());
            throw new ValidationException();
        } if (user.getBirthday().isAfter(currentMoment)){
            log.debug("Дата рождения не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException();
        }
    }
}

