package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    ArrayList<User> users=new ArrayList<>();
    @GetMapping
    public List<User> gettingAllUsers() {
        log.debug("Получен запрос GET /users");
        return users;
    }
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if(isValid(user)) {
            if (user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            log.debug("Получен запрос POST. Передан обьект {}",user);
            users.add(user);
            return user;
        }else{
            log.error("Передан запрос POST с некорректным данными.");
            throw new ValidationException("Ошибка валидации пользователя!");
        }
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user ) {
        if(isValid(user)) {
            for(User user1:users){
                if(user1.getId()== user.getId()){
                    users.set(users.indexOf(user1),user);
                    return user;
                }else{
                    log.error("Передан запрос PUT с некорректным id.");
                    throw new ValidationException("Отсутствует пользователь с данным id");
                }
            }
        }else{
            log.error("Передан запрос POST с некорректным данными.");
            throw new ValidationException("Ошибка валидации пользователя!");
        }
        return user;
    }
    private boolean isValid(User user){
        return !user.getLogin().contains(" ");
    }

}
