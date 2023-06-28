package ru.yandex.practicum.filmorete.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorete.exeptions.ValidationFilmException;
import ru.yandex.practicum.filmorete.exeptions.ValidationUserException;
import ru.yandex.practicum.filmorete.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();
    private Integer lastIdentification = 1;

    @GetMapping()
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping()
    public User create(@RequestBody User user) throws ValidationUserException {

        if (users.containsKey(user.getId())) {
            String message = String.format("Пользователь %s уже есть в системе!", user.getName());
            throw new ValidationUserException(message);

        }
        else if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new ValidationUserException("Email равен null или отсутствует!");
        }
        else {
            validatorUser(user);
            user.setId(getLastIdentification());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            log.debug("Добавлен новый пользователь: {}", user.getName());
            return user;
        }
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationUserException {
        if (user.getId() == null || user.getId() < 1) {
            throw new ValidationUserException("Не указан ID пользователя!");
        }
        validatorUser(user);
        users.put(user.getId(), user);
        log.debug("Пользователь {} успешно обновлен!", user.getName());
        return user;
    }

    private void validatorUser(User user) throws ValidationUserException {
        if (user.getBirthday() == null || user.getBirthday().equals("")) {
            throw new ValidationUserException("Дата рождения равна null или отсутствует!");
        }
        if (user.getLogin() == null || user.getLogin().equals("")) {
            throw new ValidationUserException("Login равен null или отсутствует!");
        }
        if (user.getEmail() == null || user.getEmail().equals("")) {
            throw new ValidationUserException("Email равен null или отсутствует!");
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationUserException("Пользователь не найден в системе!");
        }
        if (emails.contains(user.getEmail())) {
            throw new ValidationUserException("Электронная почта уже зарегестрированна в системе!");
        }
        if (user.getEmail().isEmpty()) {
            throw new ValidationUserException("Электронная почта не может быть пустой!");
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
        if (user.getName() == null) {
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
