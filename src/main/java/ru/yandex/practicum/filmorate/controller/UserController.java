package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;


    @GetMapping
    public List<User> getAllUser() {
        log.debug("There is {} user in filmorate", userServiceImpl.getAllUsers().size());
        return userServiceImpl.getAllUsers()
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    @PostMapping
    public User addFilm(@Valid @RequestBody User  user) {
        log.debug("User with id={} added",user.getId());
        return userServiceImpl.addUsers(user);
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody User user) {
        log.debug("User with id={} updated",user.getId());
        return userServiceImpl.updateUsers(user);
    }
}
