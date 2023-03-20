package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@Service
public interface UserService {
    User updateUsers(@Valid User user);

    User addUsers(@Valid User user);

    HashMap<Integer, User> getAllUsers();
}
