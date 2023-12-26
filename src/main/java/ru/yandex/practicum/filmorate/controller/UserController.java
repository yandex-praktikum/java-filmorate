package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserService;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", userService.getUsers().size());
        return userService.getUsers();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        validateUser(user);
        userService.add(user);
        return user;
    }

    @PutMapping("/users")
    public User saveUser(@RequestBody User user, HttpServletResponse response) {
        validateUser(user);
        if (userService.isAlreadyExists(user)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.update(user);
        } else if (!userService.isAlreadyExists(user) && (user.getId() != 0)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return user;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return userService.add(user);
        }
    }

    private void validateUser(User user) {
        if ((user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
            throw new ValidationException("E-mail is empty or not contains symbol \"@\"");
        }
        if ((user.getLogin().isEmpty()) || user.getLogin().contains(" ")) {
            throw new ValidationException("Login is empty or contains a space");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }
    }
}
