package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Data
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Integer globalId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.debug("List off all users has been requested.");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        user.setId(globalId);

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        users.put(globalId, user);
        globalId++;

        log.debug("User with id = {} has been created.", user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        Integer id = user.getId();

        checkUserExist(id);
        user.setId(id);
        users.put(id, user);

        log.debug("User with id = {} has been updated.", id);
        return user;
    }

    private void checkUserExist(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User with id %d does not exist!", id));
        }
    }
}
