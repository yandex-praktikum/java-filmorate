package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private static int idUser = 0;
    private List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user, HttpServletRequest request) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        User userForList = null;
        try {
            boolean isCorrect = validateUser(user);
            if (isCorrect) {
                if (user.getId() == 0) {
                    setIdUser(getIdUser() + 1);
                    user.setId(getIdUser());
                }
                userForList = user;
                users.add(userForList);
            }
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getDetailMessage());
        }
        return userForList;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        try {
            boolean isCorrect = validateUser(user);
            if (isCorrect) {
                boolean isContain = false;
                if (!users.isEmpty()) {
                    for (User userFromList : users) {
                        if (userFromList.getId() == user.getId()) {
                            users.remove(userFromList);
                            users.add(user);
                            isContain = true;
                            break;
                        }
                    } if (!isContain) {
                        log.info("Некорректный запрос. User c таким id не найден");
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный запрос. User c таким id не найден");
                    }
                } else {
                    log.info("User-ы отсутствуют в базе данных");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User-ы отсутствуют в базе данных");
                }
            }
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка валидации user-а");
        }
        return user;
    }

    private boolean validateUser(User user) {
        boolean isCorrect = true;
        if ((user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
            isCorrect = false;
            log.info("Ошибка валидации пользователя. Некорректный e-mail адресс");
            throw new ValidationException("Ошибка валидации пользователя. Некорректный e-mail адресс");
        } else if ((user.getLogin().isEmpty()) || (user.getLogin().contains(" "))) {
            isCorrect = false;
            log.info("Ошибка валидации пользователя. Некорректный логин");
            throw new ValidationException("Ошибка валидации пользователя. Некорректный логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            isCorrect = false;
            log.info("Ошибка валидации пользователя. Некорректная дата рождения");
            throw new ValidationException("Ошибка валидации пользователя. Некорректная дата рождения");
        }
        return isCorrect;
    }

    public static int getIdUser() {
        return idUser;
    }

    public static void setIdUser(int idUser) {
        UserController.idUser = idUser;
    }

}