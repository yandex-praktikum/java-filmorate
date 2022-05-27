package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Long, User> listUsers = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return listUsers.values();
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (!((user.getEmail() != null) && (user.getEmail().contains("@")))) {
            throw new ValidationException("Неправильный формат почты");
        }
        if ((user.getLogin() == null) && (user.getLogin().contains(" "))) {
            throw new ValidationException("Неправильный формат логина");
        }
        if ((user.getName() == null)||(user.getName().equals(""))) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        listUsers.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId()<=0){
            throw new ValidationException("id меньше или равен нулю");
        }
        if (!((user.getEmail() != null) && (user.getEmail().contains("@")))) {
            throw new ValidationException("Неправильный формат почты");
        }
        if ((user.getLogin() == null) && (user.getLogin().contains(" "))) {
            throw new ValidationException("Неправильный формат логина");
        }
        if ((user.getName() == null)||(user.getName().equals(""))) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        listUsers.put(user.getId(), user);
        return user;
    }
}
