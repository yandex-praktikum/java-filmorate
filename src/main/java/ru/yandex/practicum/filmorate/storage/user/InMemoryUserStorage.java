package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.validateUser;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final List<User> users;
    private int userId = 1;

    public InMemoryUserStorage(List<User> users) {
        this.users = users;
    }

    @Override
    public User createUser(User user){
        log.info("Получен POST-запрос с объектом User: {}", user);
        user.setId(userId);
        validateUser(user);
        users.add(user);
        log.info("Пользователь {} c id={} добавлен", user.getName(), userId);
        increaseUserId();
        return user;
    }

    @Override
    public User updateUser(User user){
        log.info("Получен PUT-запрос с объектом User: {}", user);
        validateUser(user);
        int updateId = user.getId();
        for (User u : users) {
            if (u.getId() == updateId){
            users.set(users.indexOf(u), user);
            log.info("Пользователь c id={} обновлён", updateId);
            return user;
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + updateId + " не найден");
    }

    @Override
    public String deleteUser(int id){
        for (User u : users) {
            if (u.getId() == id){
                users.remove(u);
                log.info("Пользователь c id={} удалён", id);
                return String.format("Пользователь c id=%s удалён", id);
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
    }

    @Override
    public User findUser(int id){
        for (User u : users){
            if (u.getId() == id){
                return u;
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
    }

    @Override
    public List<User> allUsers(){
        log.info("Получен GET-запрос на all");
        return users;
    }

    private void increaseUserId(){
        userId++;
    }
}
