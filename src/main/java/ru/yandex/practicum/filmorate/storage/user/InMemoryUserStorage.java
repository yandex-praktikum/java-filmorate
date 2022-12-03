package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;

import javax.validation.*;
import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private List<User> usersArrayList = new ArrayList<>();
    private int idCount = 1;

    public HashMap<Integer, User> getUsers() {
        return users;
    }
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    public List<User> findAll() {
        usersArrayList.addAll(users.values());
        return usersArrayList;
    }

    public User create(@Valid User user) throws ValidationException {
        try {
           // Optional<String> userName = Optional.of(user.getName());
            Set<ConstraintViolation<User>> validate = validator.validate(user);
            if (validate.size() > 0 || user.getLogin() == "" )
            {
                throw new ValidationException("Error while saving");
            } else {
                user.setId(idCount++);
                if (user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
            }
        }catch (ValidationException validationException) {
            throw new ValidationException(validationException.getMessage());
        }
        return user;
    }

    public User update(@Valid User user) throws ValidationException {
        try {
            Set<ConstraintViolation<User>> validate = validator.validate(user);
            if (validate.size() > 0 || user.getLogin() == "" ||
                    user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Error while updating");
            } else {
                if(users.containsKey(user.getId())) {
                    users.remove(user.getId());
                    users.put(user.getId(), user);
                } else {
                    throw new ObjectNotFoundException("Error while updating");
                }
            }
        } catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        } catch (ObjectNotFoundException exception){
            throw new ObjectNotFoundException(exception.getMessage());
        }
        return user;
    }
}
