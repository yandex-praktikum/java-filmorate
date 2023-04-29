package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    public Map<Integer, Set<Integer>> friends = new HashMap<>();
    @Autowired
    InMemoryUserStorage inMemoryUserStorage;

    public List<User> getFriends(Integer id) {
        Set<Integer> friendsOfId = friends.get(id);
        if (friendsOfId != null && !friendsOfId.isEmpty()) {
            return inMemoryUserStorage.getUsers(friendsOfId);
        }
        return Collections.EMPTY_LIST;
    }

    public void addFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id);
        inMemoryUserStorage.getUser(friendId);
        Set<Integer> thisUserFriends = friends.get(id);
        if (thisUserFriends == null) {
            thisUserFriends = new HashSet<>();
        }
        thisUserFriends.add(friendId);
        friends.put(id, thisUserFriends);
        Set<Integer> thisFriendFriends = friends.get(friendId);
        if (thisFriendFriends == null) {
            thisFriendFriends = new HashSet<>();
        }
        thisFriendFriends.add(id);
        friends.put(friendId, thisFriendFriends);
    }

    public void argueFriends(Integer id, Integer friendId) {
        inMemoryUserStorage.getUser(id);
        inMemoryUserStorage.getUser(friendId);
        Set<Integer> thisUserFriends = friends.get(id);
        thisUserFriends.remove(friendId);
        friends.put(id, thisUserFriends);
        Set<Integer> thisFriendFriends = friends.get(friendId);
        thisFriendFriends.remove(id);
        friends.put(friendId, thisFriendFriends);
    }

    public List<User> showCommonFriends(int id,  int otherId) {
        Set<Integer> thisUserFriends = friends.get(id);
        Set<Integer> thisFriendFriends = friends.get(otherId);
        if (thisUserFriends == null || thisFriendFriends == null) {
            return Collections.EMPTY_LIST;
        }
        Set<Integer> commonFriends = new HashSet<>();
        for (Integer thisUser : thisUserFriends) {
            if (thisFriendFriends.contains(thisUser)) {
                commonFriends.add(thisUser);
            }
        } //return new HashSet<User>(thisFriendFriends).retainAll(thisUserFriends);
        return inMemoryUserStorage.getUsers(commonFriends);
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User getUser(int userId) {
        return inMemoryUserStorage.getUser(userId);
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }


    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public void delete(User user) {
        Integer id = user.getId();
        for (Integer friendId: friends.get(id)){
            Set<Integer> friendFriends = friends.get(friendId);
            friendFriends.remove(id);
            friends.put(friendId, friendFriends);
        }
        inMemoryUserStorage.delete(user);
    }
}