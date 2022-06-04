package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if(users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        String checkUser = validationUser(user);
        if(!(checkUser.isBlank())){
            throw new ValidationException(checkUser);
        }
        if(user.getName() == null ||user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        user.setId(++User.countUser);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        String checkUser = validationUser(user);
        if(!(checkUser.isBlank())){
            throw new ValidationException(checkUser);
        }
        if(user.getId() < 1){
            throw new ValidationException("не корректный id user");
        }
        users.put(user.getId(), user);
        return user;
    }



    private String validationUser(User user){
        List<String> result = new ArrayList<>();
        if(Pattern.compile("\\s").matcher(user.getLogin()).find()){
            result.add("Логин не может содержать пробелы");
        }
        if(LocalDate.now().isBefore(user.getBirthday())){
            result.add("Дата рождения не может быть в будущем");
        }
        return String.join(", ", result);
    }
}












