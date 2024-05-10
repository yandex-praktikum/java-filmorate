package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.Collections;
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
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info("Creating user with login: {}", user.getLogin());
        if (bindingResult.hasErrors()) {
            log.warn("Validation createUser errors occurred: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Validation error: "
                    + bindingResult.getAllErrors()));
        }
        try {
            validateBirthDate(user);
            setDisplayName(user);
        } catch (ValidationException e) {
            log.error("Error creating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                    "Error creating user due to invalid input: " + e.getMessage()));
        }

        User createdUser = userService.createUser(user);
        log.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody User user,
                                             BindingResult bindingResult) {
        log.info("Updating user with ID: {}", id);
        if (bindingResult.hasErrors()) {
            log.warn("Validation updateUser errors occurred: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Validation error: "
                    + bindingResult.getAllErrors()));
        }
        try {
            validateBirthDate(user);
            setDisplayName(user);
        } catch (ValidationException e) {
            log.error("Error creating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                    "Error updating user due to invalid input: " + e.getMessage()));
        }
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error",
                    "User not found with ID: " + id));
        }
        log.info("User updated successfully with ID: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
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