package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или с id \"%s\" не существует."
                            , userId, friendId));
        }
    }

    public void removeFriend(Integer id1, Integer id2) {
        if (userStorage.getUsers().get(id1) != null || userStorage.getUsers().get(id2) != null) {
            userStorage.getUsers().get(id1).getFriends().remove(id2);
            userStorage.getUsers().get(id2).getFriends().remove(id1);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или с не id \"%s\" не существует."
                            , id1, id2));
        }
    }

    public List<User> getFriendsList(Integer id) {
        List<User> list = null;
        if (userStorage.getUsers().get(id) != null) {
            list = userStorage.getUserById(id).getFriends().stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\"не существует.", id));
        }
        return list;
    }

    public List<User> getMutualFriends(Integer id1, Integer id2) {
        List<User> userList = null;
        Set<Integer> setMutualFriends = new HashSet<>(userStorage.getUserById(id1).getFriends());
        setMutualFriends.retainAll(userStorage.getUserById(id2).getFriends());
        userList = setMutualFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        return userList;
    }
}