package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 0;

    @GetMapping
    public List<User> findAll() {
        log.info("Получен запрос GET/users - получение списка пользователей");
        log.info("Текущее количество пользователей: {}", users.size());
        List <User> list = new ArrayList<>();
        for(User u : users.values()) {
            list.add(u);
        }
        return list;
    }

    public Map<Long, User> findAllForTest() {
        return users;
    }

    @SneakyThrows
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST/users - создание нового пользователя");
        if(users.containsKey(user.getId())) {
            log.info("Попытка добавить пользователя с уже существующим id");
            throw new IdAlreadyExistException();
        }
        if(user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.info("Попытка создать пользователя без указания логина");
            throw new InvalidNameException();
        }
        if(user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Попытка создать пользователя с некорректным email");
            throw new InvalidEmailException();
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Попытка добавить пользователя из будущего");
            throw new InvalidBirthdateException();
        }
        if(user.getName() == null) {
            log.info("Попытка добавить пользователя с пустым полем name");
            user.setName(user.getLogin());
            log.info("В значение пустого поля name установлен логин пользователя: {}", user.getLogin());
        }

        userId++;
        user.setId(userId);
        log.info("Установлен id пользователя: {}", userId);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @SneakyThrows
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT/users - обновление пользователя с id {}", user.getId());
        if(user.getId() == null || !users.containsKey(user.getId())) {
            log.info("Попытка обновить пользователя с пустым или несущетвующим id");
            throw new InvalidIdException();
        }
        if(user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.info("Попытка создать пользователя без указания логина");
            throw new InvalidNameException();
        }
        if(user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Попытка создать пользователя с некорректным email");
            throw new InvalidEmailException();
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Попытка добавить пользователя из будущего");
            throw new InvalidBirthdateException();
        }
        if(user.getName() == null) {
            log.info("Попытка добавить пользователя с пустым полем name");
            user.setName(user.getLogin());
            log.info("В значение пустого поля name установлен логин пользователя: {}", user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

}
