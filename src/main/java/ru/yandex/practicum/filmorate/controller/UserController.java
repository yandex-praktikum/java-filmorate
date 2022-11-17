package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private List<User> usersArrayList = new ArrayList<>();
    private int idCount = 1;

    @GetMapping("/users") // получение списка пользователей
    public List<User> findAll() {
        usersArrayList.addAll(users.values());
        return usersArrayList;
    }

    @PostMapping(value = "/users") // добавление пользователя
    public User create(@RequestBody User user) throws ValidationException {
        if(!validate(user)) {
            throw new ValidationException();
        } else {
            user.setId(idCount);
            idCount++;
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping(value = "/users") // обновление пользователя
    public User update(@RequestBody User user) throws ValidationException {
        try {
            if (users.containsKey(user.getId())) {
                if (!validate(user)) {
                    throw new ValidationException("Update was cancelled");
                } else {
                    users.remove(user.getId());
                    users.put(user.getId(), user);
                }
            } else {
                throw new ValidationException("Update was cancelled");
            }
        } catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        }
        return user;
    }

    private boolean validate(User user) throws ValidationException {
        boolean check = false;
        try {
            Optional<String> userName = user.getName();
            Optional<String> userEmail = user.getEmail();
            Optional<String> userLogin = user.getLogin();
            if (userEmail==null || !userEmail.get().contains("@")) {
                throw new ValidationException("The email is incorrect.");
            } else if (userLogin==null || userLogin.get().contains(" ")) {
                throw new ValidationException("The login can't be empty and contain spaces");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("The user's birthday can't be in the future");
            } else {
                check = true;
                if (userName==null) {
                    user.setName(user.getLogin());
                }
            }
        } catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        }
        return check;
    }

}
