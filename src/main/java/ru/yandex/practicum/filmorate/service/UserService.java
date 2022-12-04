package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ActionHasAlreadyDoneException;

import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    List<User> commonFriends = new ArrayList<>();

    public User findUser(int id) { // получение пользователя по ID
            if (!userStorage.getUsers().containsKey(id)) {
                throw new ObjectNotFoundException("There no user with such id");
            }
        return userStorage.getUsers().get(id);
    }

    public void addToFriendList(int id, int friendId) { //добавление в друзья
            if(!findUser(id).getFriends().isEmpty() && findUser(id).getFriends().contains((long)friendId)) {
                throw new ActionHasAlreadyDoneException("Friend has already added");
            } else if(friendId<=0) {
                throw new ObjectNotFoundException("Wrong id.");
            } else {
                findUser(id).setFriends(friendId);
                findUser(friendId).setFriends(id);
            }
    }

    public void removeFromFriendList(int id, int friendId) {
        findUser(id).deleteFriend(friendId);
        findUser(friendId).deleteFriend(id);

    }

    public List<User> getFriendList(int id) {
        List<User> friends = new ArrayList<>();
        for (Long i : findUser(id).getFriends()) {
            friends.add(userStorage.getUsers().get(Math.toIntExact(i)));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {

            commonFriends.clear();
            if(findUser(id).getFriends().isEmpty()) {
                return commonFriends;
            }
            commonFriends.addAll(getFriendList(id));
            commonFriends.retainAll(getFriendList(otherId));
        return commonFriends;
    }

}
