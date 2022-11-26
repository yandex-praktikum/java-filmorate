package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

//электронная почта не может быть пустой и должна содержать символ @;
//логин не может быть пустым и содержать пробелы;
//имя для отображения может быть пустым — в таком случае будет использован логин;
//дата рождения не может быть в будущем.
@Slf4j
public class UserDataValidate {

    private final User user;

    public UserDataValidate(User user) {
        this.user = user;
    }

    public boolean checkAllData() {
        if(isCorrectLogin() && isCorrectEmail() && isCorrectBirthday()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCorrectEmail() {//электронная почта не может быть пустой и должна содержать символ @;
        if(!user.getEmail().isEmpty() && user.getEmail().contains("@")) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Электронная почта пустая или не содержит @");
            return false;
        }
    }

    private boolean isCorrectLogin() {//логин не может быть пустым и содержать пробелы;
        if(!user.getLogin().isEmpty() && !user.getLogin().contains(" ")) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Логин пустой или содержит пробелы");
            return false;
        }
    }

    private boolean isCorrectBirthday() {
        if(user.getBirthday().isBefore(LocalDate.now())) {//дата рождения не может быть в будущем.
            return true;
        } else {
            log.warn("Ошибка во входных данных. Дата рождения указана в будующем");
            return false;
        }
    }
}
