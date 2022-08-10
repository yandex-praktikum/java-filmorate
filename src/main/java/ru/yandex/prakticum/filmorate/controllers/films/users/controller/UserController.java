package ru.yandex.prakticum.filmorate.controllers.films.users.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prakticum.filmorate.controllers.films.users.model.User;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class UserController {
    private Map<Integer,User> users;

    @PutMapping("/user/")
    public String createUser(@RequestBody User user){
        if (UserCheck.userCheck(user)){
            if (users.containsKey(user.getId())){
                log.trace("Добавлен " + user);
                users.put(user.getId(),user);
                return "Добавлен " + user;
            }
            else {
                log.trace("Изменен " + user);
                users.replace(user.getId(),user);
                return "Изменен " + user;
            }
        }
        return "Валидация не пройдена";
    }

    @GetMapping("/user")
    public List getAllUser(){
        return List.of(users.values());
    }


}
