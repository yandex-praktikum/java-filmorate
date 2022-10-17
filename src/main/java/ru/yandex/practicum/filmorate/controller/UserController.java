package ru.yandex.practicum.filmorate.controller;

import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController{
    private final List<User> users = new ArrayList<>();
    private int userId = 1;

    private void increaseUserId(){
        userId++;
    }

    @GetMapping
    public List<User> allUsers(){
        log.info("Получен GET-запрос на all");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        log.info("Получен POST-запрос с объектом User: {}", user);
        user.setId(userId);
        validate(user);
        users.add(user);
        log.info("Пользователь {} c id={} добавлен", user.getName(), userId);
        increaseUserId();
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        log.info("Получен PUT-запрос с объектом User: {}", user);
        validate(user);
        int userId = user.getId();
        for (User u : users) {
            if (u.getId() == userId) {
                users.set(users.indexOf(u), user);
                return user;
            }
        }
        throw new ValidationException("Пользователь с id=" + userId + " не найден");
    }

    public void validate(User user) throws ValidationException {
        if (StringUtils.hasLength(user.getEmail()) && user.getEmail().contains("@")){
            if (StringUtils.hasLength(user.getLogin()) && !user.getLogin().contains(" ")){
                if (user.getBirthday().isBefore(LocalDate.now().plusDays(1))){
                    if (!StringUtils.hasLength(user.getName())) user.setName(user.getLogin());
                } else throw new ValidationException("Дата рождения не может быть в будущем");
            } else throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else throw new ValidationException("Почта не может быть пустой и должна содержать символ @");
    }
}
