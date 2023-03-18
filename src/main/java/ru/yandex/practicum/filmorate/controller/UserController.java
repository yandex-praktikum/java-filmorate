package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> userHashMap = new HashMap<>();

    @GetMapping
    public HashMap<Integer, User> getUsers () {
        return userHashMap;
    }

    @PostMapping
    public ResponseEntity<String> addUser (@RequestBody User user) {
        userHashMap.put(user.getId(),user);
        return ResponseEntity.ok("Valid");
    }
}
