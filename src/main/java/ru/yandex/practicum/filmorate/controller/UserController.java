package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private List<User> usersArrayList = new ArrayList<>();
    private int idCount = 1;
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @GetMapping("/users") // получение списка пользователей
    public List<User> findAll() {
        usersArrayList.addAll(users.values());
        return usersArrayList;
    }

    @PostMapping(value = "/users") // добавление пользователя
    public User create(@Valid @RequestBody User user) throws ValidationException {
        try {
            Optional<String> userName = user.getName();
            Set<ConstraintViolation<User>> validate = validator.validate(user);
            if (validate.size() > 0 || user.getLogin() == "" ||
                    user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Error while saving");
            } else {
                user.setId(idCount++);
                if (userName==null || userName.isEmpty()) {
                    user.setName(Optional.of(user.getLogin()));
                }
                users.put(user.getId(), user);
            }
        }catch (ValidationException validationException) {
            throw new ValidationException(validationException.getMessage());
        }
        return user;
    }

    @PutMapping(value = "/users") // обновление пользователя
    public User update(@Valid @RequestBody User user) throws ValidationException {
        try {
            Set<ConstraintViolation<User>> validate = validator.validate(user);
            if (validate.size() > 0 || user.getLogin() == "" ||
                    user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Error while updating");
            } else {
                if(users.containsKey(user.getId())) {
                    users.remove(user.getId());
                    users.put(user.getId(), user);
                } else {
                    throw new ValidationException("Error while updating");
                }
            }
        } catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        }
        return user;
    }
}
