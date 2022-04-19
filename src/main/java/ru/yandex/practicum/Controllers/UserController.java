package ru.yandex.practicum.Controllers;

import lombok.Data;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Data
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<String, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Количество пользователей на текущий момент {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.info("Не валидный адрес электронной почты.");
            throw new ValidationException("Не валидный адрес электронной почты.");
        }

        if (users.containsKey(user.getEmail())) {
            log.info("Пользователь с email " + user.getEmail() +
                    " уже существует.");
            throw new ValidationException("Пользователь с email " + user.getEmail() +
                    " уже существует.");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано, вместо имени будет использован логин.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        user.setId(id);
        id++;
        users.put(user.getEmail(), user);
        log.info("Создан пользователь {}", user, toString());
        return user;
    }

    @PutMapping
    public User change(@Valid @RequestBody User user) {
        if (user.getName() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        users.put(user.getEmail(), user);
        log.info("Пользователь " + user.getEmail() + " изменен.");

        return user;
    }

}
