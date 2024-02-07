package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.validate.annotation.BirthdayValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthdayValidator implements ConstraintValidator<BirthdayValidation, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext constraintValidatorContext) {
        return birthday.isBefore(LocalDate.now());
    }
}
