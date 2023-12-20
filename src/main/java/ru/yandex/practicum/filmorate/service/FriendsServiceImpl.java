package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendsServiceImpl implements FriendsService {

    private final UserStorage userStorage;

    @Override
    public List<User> addFriend(Integer id, Integer friendId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(friendId);

        return userStorage.addFriend(id, friendId);
    }

    @Override
    public List<User> deleteFriend(Integer id, Integer friendId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(friendId);

        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        userStorage.checkUserExist(id);

        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(otherId);

        return userStorage.getCommonFriends(id, otherId);
    }
}
