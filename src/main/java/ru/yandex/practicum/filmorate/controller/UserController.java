package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("GET /users - запрос всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Post /users создание нового пользователя: {}", user.getName());
        String userEmail = user.getEmail();
        String userLogin = user.getLogin();
        String userName = user.getName();
        LocalDate userBirthday = user.getBirthday();
        LocalDate today = LocalDate.now();
        if ((userEmail == null) || (userEmail.isBlank()) || (!userEmail.contains("@"))) {
            log.warn("Ошибка валидации email, получен: {}", userEmail);
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if ((userLogin == null) || (userLogin.isBlank())) {
            log.warn("Ошибка валидации login, получен: {}", userLogin);
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }
        if ((userName == null) || (userName.isBlank())) {
            log.debug("Имя пользователя заменено на логин: {}", userLogin);
            user.setName(userLogin);
        }
        if ((userBirthday != null) && (userBirthday.isAfter(today))) {
            log.warn("Ошибка валидации дня рождения пользователя, получено: {}", userBirthday);
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }

        user.setId(getNextId());
        log.debug("Пользователю сгенерирован id: {}", user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь с id {} добавлен во внутреннее хранилеще", user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        log.info("PUT /users - обновление пользователя с id: {}", newUser.getId());
        Long newUserId = newUser.getId();
        if (newUserId == null) {
            log.warn("При изменении пользователя не передали id");
            throw new ConditionsNotMetException("id должен быть указан");
        }

        if (users.containsKey(newUserId)) {
            String newUserEmail = newUser.getEmail();
            String newUserLogin = newUser.getLogin();
            String newUserName = newUser.getName();
            LocalDate newUserBirthday = newUser.getBirthday();
            User oldUser = users.get(newUserId);
            if ((newUserName == null) || (newUserBirthday == null) ||
                    (newUserLogin == null) || (newUserEmail == null)
            ) {
                log.info("Не все данные для изменения пользователя были переданы");
                return oldUser;
            }
            oldUser.setBirthday(newUserBirthday);
            oldUser.setLogin(newUserLogin);
            oldUser.setEmail(newUserEmail);
            oldUser.setName(newUserName);
            log.info("Пользователь с id: {} успешно обновлён", newUserId);
            return oldUser;
        } else {
            log.warn("Пользователь с id: {} не был найден", newUserId);
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    private long getNextId() {
        long curMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++curMaxId;
    }
}
