package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Long, User> users;
    private static long idCounter = 0;

    public UserController() {
        users = new HashMap<>();
    }
    private static long createID()
    {
        return idCounter++;
    }
    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        if(users.values().stream()
                .filter(x->x.getLogin().equalsIgnoreCase(user.getLogin()))
                .anyMatch(x->x.getEmail().equalsIgnoreCase(user.getEmail()))){
            log.error("Пользователь '{}' с элетронной почтой '{}' уже существует.",
                    user.getLogin(), user.getEmail());
            throw new ValidationException("This user already exists");
        }
        if (isValidate(user)) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())){
            log.error("Пользователь '{}' c id '{}' не найден", user.getLogin(), user.getId());
            throw new ValidationException("User not found in the map");
        }
        if (isValidate(user)) {
            user.setId(createID());
            users.put(user.getId(), user);
            log.info("Данные пользователя '{}' обновлены", user.getLogin());
        }
        return user;
    }

    @GetMapping("/users")
    public Map<Long, User> getAllUsers() {
        return users;
    }

    private boolean isValidate(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Некорректный адрес электронной почты");
            throw new ValidationException("invalid email");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не должен быть пустым и не должен содержать пробелов");
            throw new ValidationException("invalid login");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("invalid birthday");
        } else {
            if (user.getName().isBlank()) user.setName(user.getLogin());
            return true;
        }
    }
}

