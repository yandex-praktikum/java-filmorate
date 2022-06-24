package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(UserStorage storage, FriendsStorage friendsStorage) {
        this.storage = storage;
        this.friendsStorage = friendsStorage;
    }


    //работа с сохранинем/обновление пользователей

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUser(Long id) {
        return storage.getUser(id);
    }

    public User create(User user) {
        return storage.create(setNameIfNameIsBlank(user));
    }

    public User update(User user) {
        return storage.update(setNameIfNameIsBlank(user));
    }

    /**
     * Если имя имя для отображения User пустое, будет использован логин.
     *
     * @param user объект для проверки.
     * @return Объект User с не пустым именем.
     */
    private User setNameIfNameIsBlank(User user) {
        if (user.getName().isBlank()) {
            return new User(user.getId(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getLogin(),
                    user.getBirthday());
        }
        return user;
    }


    //работа c дружескими связями между пользователями

    public boolean addFriend(Long userId, Long friendId) {
        return friendsStorage.addFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        return friendsStorage.getFriends(userId);
    }

    public boolean removeFriend(Long userId, Long friendId) {
        return friendsStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        return friendsStorage.getCommonFriends(userId, otherId);
    }
}
