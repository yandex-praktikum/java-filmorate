package ru.yandex.practicum.filmorete.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorete.exeptions.ValidationUserException;
import ru.yandex.practicum.filmorete.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private Map<String, User> users = new HashMap<>();
    private Integer lastIdentification = 1;

    @GetMapping("/users")
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping("/user")
    public User create(@RequestBody User user) throws ValidationUserException {
        if (users.containsKey(user.getEmail())) {
            String message = String.format("Пользователь {} уже есть в системе!", user.getName());
            throw new ValidationUserException(message);

        }
        else if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new ValidationUserException("Email равен null или отсутствует!");
        }
        else {
            validatorUser(user);
            user.setId(getLastIdentification());
            users.put(user.getEmail(), user);
            log.debug("Добавлен новый пользователь: {}", user.getName());
            return user;
        }
    }

    @PutMapping("/user")
    public User update(@RequestBody User user) throws ValidationUserException {
        if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new ValidationUserException("Email равен null или отсутствует!");
        }
        validatorUser(user);
        users.put(user.getEmail(), user);
        log.debug("Пользователь {} успешно обновлен!", user.getName());
        return user;
    }

    private void validatorUser(User user) throws ValidationUserException {
        if (user.getEmail().isEmpty()) {
            throw new ValidationUserException("Название фильма не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationUserException("Электронная почта должна содержать символ '@'!");
        }
        if (user.getLogin().isEmpty()) {
            throw new ValidationUserException("Логин не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationUserException("Логин не должен содержать пробелы!");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationUserException("Дата рождения не должна быть в будущем!");
        }
    }

    private Integer getLastIdentification() {
        return lastIdentification++;
    }
}
