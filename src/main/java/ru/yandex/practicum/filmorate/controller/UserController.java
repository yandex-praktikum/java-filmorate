package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private Integer currentId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user){
        user.setId(currentId);
        users.put(currentId, user);
        currentId++;
        return user;
    }

    @PutMapping("/{id}")
    public User update(@RequestParam Integer id, @RequestBody User user) {
        checkUserExist(id);
        user.setId(id);
        users.put(id, user);

        return user;
    }

    private void checkUserExist(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserDoesNotExistException(String.format("User with id %d does not exist!", id));
        }
    }
}
