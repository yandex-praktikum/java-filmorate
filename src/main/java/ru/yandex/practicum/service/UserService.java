package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.FriendStatus;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс пользователей Filmorate
 */
@Slf4j
@Service
public class UserService {
    private final UserStorage storage;
    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    /**
     * Добавление пользователя в друзья
     */
    public User addFriend(User user, User friend) {
        user.getFriends().put(friend.getId(), FriendStatus.НЕПОДТВЕРЖДЕННАЯ);
        friend.getFriends().put(user.getId(), FriendStatus.НЕПОДТВЕРЖДЕННАЯ);
        log.debug("Добавление в друзья пользователя с id {}", friend);
        return user;
    }

    /**
     * Удаление пользователей из друзей
     */
    public User deleteFriend(User user, User friend) {
        log.debug("Удаление из друзей пользователя c id {}", friend);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user;
    }

    /**
     * Получение списка друзей
     */
    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<>();
            for (User u : storage.findAll()) {
                if (u.getFriends().keySet().contains(user.getId())) {
                    friends.add(u);
                }
            }
        log.debug("Получение списка друзей {}", friends);
        return friends;
    }

    /**
     * Получение списка общих друзей
     */
    public List<Integer> getCommonFriends(User user1, User user2) {
        log.debug("Получение списка общих друзей пользователей {} и ", user1, " {}", user2);
        return user1.getFriends().keySet()
                .stream()
                .filter(user2.getFriends().keySet()::contains)
                .collect(Collectors.toList());
    }
}
