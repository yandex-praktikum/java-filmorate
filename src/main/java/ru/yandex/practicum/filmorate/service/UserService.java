package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User getUserById(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        return userStorage.getUserById(id);
    }

    public User makeFriendship(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Невозможно добавить в друзья несуществующего юзера!");
        }
        userStorage.getUserById(id).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(id);
        return userStorage.getUserById(id);
    }

    public User deleteFriendship(long id, long friendId) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Добавляемого в друзья юзера нет в списке!");
        }
        userStorage.getUserById(id).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(id);
        return userStorage.getUserById(id);
    }

    public Collection<User> listOfFriends(long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (userStorage.getUserById(id).getFriends() == null) {
            throw new NotFoundException("Список друзей пуст!");
        }
        return userStorage.getUserById(id).getFriends().stream()
                .map(friends -> userStorage.getUserById(friends))
                .collect(Collectors.toList());
    }

    public Collection<User> listOfCommonFriends(Long id, Long otherId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(otherId) == null) {
            throw new NotFoundException("Одного из юзеров нет в списке!");
        }
        if (userStorage.getUserById(id).getFriends() == null || userStorage.getUserById(otherId).getFriends() == null) {
            throw new NotFoundException("Один из списков друзей пуст!");
        }
        userStorage.getUserById(id).getFriends().retainAll(userStorage.getUserById(otherId).getFriends());
        return userStorage.getUserById(id).getFriends().stream()
                .map(friends -> userStorage.getUserById(friends))
                .collect(Collectors.toList());
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}