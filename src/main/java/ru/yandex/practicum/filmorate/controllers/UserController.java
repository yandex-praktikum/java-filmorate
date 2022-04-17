package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidatior;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Getter
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

   UserValidatior userValidatior;

//    public UserController() {
//        this.userValidatior = new UserValidatior();
//    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("findAll users");
        return users.values();
    }

    @PostMapping
    public User create(@Valid  @RequestBody User user) {
        userValidatior.validate(user);
       log.info("Добавляемый user: {}",user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        userValidatior.validate(user);
        log.info("Добавляемый user: {}",user);
        users.put(user.getId(), user);
        return user;
    }
}
