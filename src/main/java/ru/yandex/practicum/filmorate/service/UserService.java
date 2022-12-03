package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ActionHasAlreadyDoneException;

import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    List<User> commonFriends = new ArrayList<>();

    public User findUser(int id) { // получение пользователя по ID
        try{
            if (!inMemoryUserStorage.getUsers().containsKey(id)) {
                throw new ObjectNotFoundException("There no user with such id");
            }
        } catch (ObjectNotFoundException exception) {
            throw new ObjectNotFoundException(exception.getMessage());
        }
        return inMemoryUserStorage.getUsers().get(id);
    }

    public void addToFriendList(int id, int friendId) { //добавление в друзья
        try {
            if(!findUser(id).getFriends().isEmpty() && findUser(id).getFriends().contains((long)friendId)) {
                throw new ActionHasAlreadyDoneException("Friend has already added");
            } else if(friendId<=0) {
                throw new ObjectNotFoundException("Wrong id.");
            } else {
                findUser(id).setFriends(friendId);
                findUser(friendId).setFriends(id);
            }
        } catch (ActionHasAlreadyDoneException exception) {
            throw new ActionHasAlreadyDoneException(exception.getMessage());
        } catch (ObjectNotFoundException exception) {
            throw new ObjectNotFoundException(exception.getMessage());
        }
    }

    public void removeFromFriendList(int id, int friendId) {
        findUser(id).deleteFriend(friendId);
    }

    public List<User> getFriendList(int id) {
        List<User> friends = new ArrayList<>();
        for (Long i : findUser(id).getFriends()) {
            friends.add(inMemoryUserStorage.getUsers().get(Math.toIntExact(i)));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {

            commonFriends.clear();
            if(findUser(id).getFriends().isEmpty()) {
                return commonFriends;
            }
            for (int i = 0; i < getFriendList(id).size(); i++) { //прохожусь по списку друзей i-го юзера
                for(int j = 0; j < getFriendList(otherId).size(); j++) {
                    if (getFriendList(otherId).get(j).equals(getFriendList(id).get(i))) { //если список друзей другого юзера
                        // содержит i-го друга нашего юзера
                        commonFriends.add(getFriendList(id).get(i));
                    }
                }
            }
        return commonFriends;
    }

}
