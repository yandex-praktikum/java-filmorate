package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        log.debug("UserController initialized with UserService.");
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating user with login: {}", user.getLogin());
        validateBirthDate(user);
        setDisplayName(user);

        User createdUser = userService.createUser(user);
        log.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Updating user with ID: {}", id);
        validateBirthDate(user);
        setDisplayName(user);

        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("User updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Fetching all users.");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    private static void validateBirthDate(User user) {
        LocalDate now = LocalDate.now();
        if (user.getBirthday().isAfter(now)) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    private void setDisplayName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.error("Attempted to create/update user with a birthday in the future: {}", user.getBirthday());
            user.setName(user.getLogin());
        }
    }

}