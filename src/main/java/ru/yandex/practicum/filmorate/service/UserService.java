package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(Integer id, Integer friendId) {
        if (isUsersNotNull(id, friendId)){
            getFriendsId(id).add(friendId);
            getFriendsId(friendId).add(id);
            return String.format("Пользователь с id=%s добавлен в список друзей.", friendId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }

    public String deleteFriend(Integer id, Integer friendId){
        if (isUsersNotNull(id, friendId)){
            getFriendsId(id).remove(friendId);
            getFriendsId(friendId).remove(id);
            return String.format("Пользователь с id=%s удалён из списка друзей.", friendId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }

    public List<User> receiveFriends(Integer id){
        if (userStorage.findUser(id) != null){
            Set<Integer> friendsId = getFriendsId(id);
            return getListOfFriends(friendsId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }

    public List<User> receiveCommonFriends(Integer id, Integer otherId) {
        if (isUsersNotNull(id, otherId)){
            Set<Integer> common = new HashSet<>(getFriendsId(id));
            common.retainAll(getFriendsId(otherId));
            return getListOfFriends(common);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }

    public List<User> getListOfFriends(Set<Integer> friendsId){
        List<User> listOfFriends = new ArrayList<>();
        if (friendsId != null && !friendsId.isEmpty()){
            for (Integer i : friendsId){
                for (User u : userStorage.allUsers()){
                    if (u.getId() == i){
                        listOfFriends.add(u);
                    }
                }
            }
        }
        return listOfFriends;
    }

    public Set<Integer> getFriendsId(Integer id){
        return userStorage.findUser(id).getFriends();
    }

    public boolean isUsersNotNull(Integer id, Integer otherId){
        return userStorage.findUser(id) != null && userStorage.findUser(otherId) != null;
    }

    public List<User> allUsers(){
        return userStorage.allUsers();
    }

    public User findUser(int id){
        return userStorage.findUser(id);
    }

    public User createUser(User user){
        return userStorage.createUser(user);
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public String deleteUser(int id){
        return userStorage.deleteUser(id);
    }
}
