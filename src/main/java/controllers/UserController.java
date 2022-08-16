package controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import model.Film;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
// branch controllers-films-users
 private List<User> users = new ArrayList<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        users.add(user);
        return user;
    }

    @PatchMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.add(user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return users;
    }
}
