package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserStorage;

import javax.validation.Valid;
import java.util.Map;


@RestController
@Slf4j
public class UserController {
    private final UserStorage inMemoryUserStorage;


    @Autowired
    public UserController(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // получить всех пользователей
    @GetMapping("/users")
    public Map<Integer, User> allUsers(){
        return inMemoryUserStorage.getAllUsers();
    }

    // добавить пользователя
    @PostMapping("/users")
    public @Valid User addUser(@Valid @RequestBody User user){
        inMemoryUserStorage.addUser(user);
        return user;
    }

    // изменить пользователя
    @ResponseBody
    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user){
        return inMemoryUserStorage.changeUser(user);
    }

    // изменить - удалить пользователя
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        inMemoryUserStorage.deleteUser(id);
    }

}
