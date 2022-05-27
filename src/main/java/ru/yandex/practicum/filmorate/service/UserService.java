package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public User addFriend(Long idUser, Long idFriend) {
        Map<Long, User> users = userStorage.getAllUsers();
        checkUserContainsInMap(users, idUser);
        checkUserContainsInMap(users, idFriend);
        User user = users.get(idUser);
        User friend = users.get(idFriend);
        HashSet<Long> userFriends = user.getFriends();
        HashSet<Long> friendFriends = friend.getFriends();
        userFriends.add(idFriend);
        friendFriends.add(idUser);
        return friend;
    }

    public void deleteFriend(Long idUser, Long idFriend) {
        Map<Long, User> users = userStorage.getAllUsers();
        checkUserContainsInMap(users, idUser);
        users.get(idUser).getFriends().remove(idFriend);
        users.get(idFriend).getFriends().remove(idUser);
    }

    public List<User> getFriends(Long id) {
        Map<Long, User> users = userStorage.getAllUsers();
        checkUserContainsInMap(users, id);
        return users.get(id).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Map<Long, User> users = userStorage.getAllUsers();
        checkUserContainsInMap(users, id);
        checkUserContainsInMap(users, otherId);
        User user = users.get(id);
        User otherUser = users.get(otherId);
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(i-> userStorage.getAllUsers().get(i))
                .collect(Collectors.toList());
    }

    private void checkUserContainsInMap(Map<Long, User> users, long id){
        if (!(users.containsKey(id))){
            log.error("Пользователь с id '{}' не найден.", id);
            throw new UserNotFoundException(
                    String.format("Пользователь с id:'%d' не найден.", id)
            );
        }
    }
}
