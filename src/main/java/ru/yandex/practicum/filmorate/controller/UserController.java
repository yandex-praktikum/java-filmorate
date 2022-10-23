package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.checkId;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController{
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Integer id){
        checkId(id);
        return userService.findUser(id);
    }

    @GetMapping
    public List<User> allUsers(){
        return userService.allUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id){
        checkId(id);
        return userService.deleteUser(id);
    }


    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        checkId(id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        checkId(id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> receiveFriends(@PathVariable Integer id){
        checkId(id);
        return userService.receiveFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> receiveCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        checkId(id, otherId);
        return userService.receiveCommonFriends(id, otherId);
    }
}
