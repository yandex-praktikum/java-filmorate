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

    @Override
    public @Valid User updateUsers(@Valid User user) {
        User userToUpdate = userHashMap.get(user.getId());
        if (userToUpdate == null) {
            log.error("Film don't find");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Film don't find");
        } else if (userToUpdate.getName() == null) {

            userHashMap.put(user.getId(), user);
            return user;
        } else {
            userHashMap.remove(userToUpdate);
            userHashMap.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public @Valid User addUsers(@Valid User user) {
        userHashMap.put(user.getId(), user);
        return user;
    }

    @Override
    public HashMap<Integer, User> getAllUsers() {
        return userHashMap;
    }


}
