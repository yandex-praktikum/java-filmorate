package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.NotFutureBirthday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotFutureBirthdayValidator implements ConstraintValidator<NotFutureBirthday, String> {

    @Override
    public boolean isValid(String birthday, ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.parse(birthday).isBefore(LocalDate.now());
    }
}