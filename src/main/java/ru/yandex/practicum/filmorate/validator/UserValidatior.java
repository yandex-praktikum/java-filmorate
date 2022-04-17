package ru.yandex.practicum.filmorate.validator;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public  class UserValidatior {
    public void validate (User user) {
       if(user.getName().isBlank()){
           user.setName(user.getLogin());
       }
    }
}
