package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {
    public HashMap<Integer, User> userHashMap = new HashMap<>();
    private static int id = 1;

    @Override
    public @Valid User updateUsers(@Valid User user) {
        User userToUpdate = userHashMap.get(user.getId());
        if (userToUpdate == null) {
            log.error("User don't find");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User don't find");
        }
        else if (userToUpdate.getName()==null || userToUpdate.getName().equals("")) {
            user.setName(user.getLogin());
            userHashMap.put(user.getId(), user);
            return user;
        }
        else {
            userHashMap.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public @Valid User addUsers(@Valid User user) {
        if (user.getId() == 0) {
            user.setId(id++);
        }
        if (user.getName()==null || user.getName().equals("")) {
            user.setName(user.getLogin());
            }
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public HashMap<Integer, User> getAllUsers() {
        return userHashMap;
    }


}
