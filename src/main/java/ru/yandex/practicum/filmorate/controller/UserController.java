package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.debug("List off all users has been requested.");

        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);

        log.debug("User with id = {} has been created.", createdUser.getId());
        return createdUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        User updatedUser = userService.update(user);

        log.debug("User with id = {} has been updated.", updatedUser);
        return updatedUser;
    }
}
