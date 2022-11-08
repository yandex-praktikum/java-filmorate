package filmorate.controller;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private int startedId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Количество пользователей до добавления: {}", users.size());
        if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now()))) {
            log.debug("Переданы некорректные данные.");
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
        user.setId(createId());
        users.put(user.getId(), user);
        log.debug("Количество пользователей после добавления: {}", users.size());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
        users.put(user.getId(), user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private int createId() {
        return startedId++;
    }
}
