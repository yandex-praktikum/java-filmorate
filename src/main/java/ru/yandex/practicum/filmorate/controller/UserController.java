package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.UserControllerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.validator.exception.ValidatorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.exception.UserControllerException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.controller.exception.UserControllerException.USER_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private long uniqueID = 1;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            UserValidator.validate(user);

            if (users.containsKey(user.getId())) {
                throw new UserControllerException(
                        String.format(USER_ALREADY_EXISTS, user));
            }

            user.setId(uniqueID++);
            users.put(user.getId(), user);
            log.trace("Успешно добавлен пользователь: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (UserControllerException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера: " + e.getMessage(), e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        try {
            UserValidator.validate(user);

            if (!users.containsKey(user.getId())) {
                throw new UserControllerException(
                        String.format(USER_NOT_FOUND, user));
            }

            users.put(user.getId(), user);
            log.trace("Пользователь успешно обновлён: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить пользователя: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (UserControllerException e) {
            log.warn("Не удалось обновить пользователя: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера: " + e.getMessage(), e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    @GetMapping
    public List<User> findAll() {
        log.trace("Возвращены все пользователи.");
        return new ArrayList<>(users.values());
    }
}