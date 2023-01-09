package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserDBService implements UserService {

    private final UserStorage userStorage;

    public UserDBService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Optional<User> getById(Long id) {
        return userStorage.getById(id);
    }

    @Override
    public Optional<User> createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public void deleteAll() {
        userStorage.deleteAll();
    }

    @Override
    public void addFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.addFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }

    }

    @Override
    public void removeFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.deleteFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> list = userStorage.getFriends(id);
        return list;
    }

    @Override
    public List<User> getCommonFriends(Long id1, Long id2) {
        List<User> commonFriends = userStorage.getCommonFriends(id1, id2);
        return commonFriends;
    }


}
