package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.Exception;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserStorage implements UserInterface{

    private final Map<Integer, User> users = new HashMap<>();
    private static Integer userId = 1;

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        if (checkValidationUser(user)){
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("Успешное добавление пользователя");
            return user;
        }
        return user;
    }

    @Override
    public void deleteUser(Integer idUser) {
        users.remove(idUser);
    }

    @Override
    public User changeUser(User user) {
        if (!users.containsKey(user.getId())){
            throw new Exception(String.format("Пользователя с id %s нет", user.getId()));
        }
        if (checkValidationUser(user)){
            users.put(user.getId(), user);
            log.info("Успешное изменение пользователя");
        }
        return user;
    }

    private boolean checkValidationUser(User user){
        if (user.getBirthDay().isAfter(LocalDate.now())){
            log.warn("дата рождения не может быть в будущем");
            throw new Exception("дата рождения не может быть в будущем");
        }
        return true;
    }

    private static Integer nextId(){
        return userId++;
    }
}
