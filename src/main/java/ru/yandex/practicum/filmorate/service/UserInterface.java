package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserInterface {

    Map<Integer, User> getAllUsers();

    User addUser(User user);

    void deleteUser(Integer idUser);

    User changeUser(User user);
}