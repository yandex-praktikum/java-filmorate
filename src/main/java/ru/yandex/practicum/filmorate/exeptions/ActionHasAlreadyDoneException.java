package ru.yandex.practicum.filmorate.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "This action has already done.")
public class ActionHasAlreadyDoneException extends RuntimeException{
    public ActionHasAlreadyDoneException(String message) {
        super(message);
    }
}
