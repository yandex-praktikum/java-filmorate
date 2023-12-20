package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(Integer id) {
        User result = userStorage.findById(id);

        if (result == null) {
            throw new NotFoundException(String.format("User with id = %d is not found.", id));
        }

        return result;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        userStorage.checkUserExist(user.getId());

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        return userStorage.update(user);
    }
}
