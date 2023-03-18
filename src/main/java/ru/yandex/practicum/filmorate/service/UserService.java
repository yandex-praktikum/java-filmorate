package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@Service
public interface UserService {
    User updateUsers(@Valid User user);
    User addUsers(@Valid User user);
    HashMap<Integer, User> getAllUsers();
}
