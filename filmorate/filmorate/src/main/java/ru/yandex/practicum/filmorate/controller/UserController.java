package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id=0;

    @PostMapping(value = "/users")
    public User addNewUser(@RequestBody User user) throws ValidationException {
        validate(user);
        if (user.getName() == null){
            user.setName(user.getLogin());
        }
        if(users.containsKey(user.getId())){
            throw new ValidationException("User - уже есть в базе", user.getId());
        }else {
            id++;
            user.setId(id);
            users.put(id, user);
            return user;
        }
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        validate(user);

        if (user.getName() == null){
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }else{
            throw new ValidationException("User нет в базе", user.getId());
        }
    }

    @GetMapping(value = "/users")
    public List<User> getUsersList() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) throws ValidationException {
        boolean isUserEmailEmpty = user.getEmail() == null || user.getEmail().isEmpty();

        boolean isUserEmailContainsAt = user.getEmail().contains("@");

        boolean isUserLoginCorrect = user.getLogin() != null && user.getLogin().matches("\\S+");

        boolean isDateBirthInFuture = user.getBirthday().after(new Date());

        if (isUserEmailEmpty) {
            throw new ValidationException("email не может быть пустым", user.getId());
        } else if (!isUserEmailContainsAt) {
            throw new ValidationException("email должен соджержать @", user.getId());
        }

        if (!isUserLoginCorrect) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы;", user.getId());
        }

        if (isDateBirthInFuture) {
            throw new ValidationException("дата рождения не может быть в будущем", user.getId());
        }
    }
}
