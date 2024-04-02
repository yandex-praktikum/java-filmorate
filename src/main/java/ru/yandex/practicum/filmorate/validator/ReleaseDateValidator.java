package ru.yandex.practicum.filmorate.validator;


import ru.yandex.practicum.filmorate.annotation.NotReleaseDateBefore1895;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<NotReleaseDateBefore1895, String> {

    @Override
    public boolean isValid(String releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.parse(releaseDate).isAfter(LocalDate.of(1895, 12, 28))
                || LocalDate.parse(releaseDate).equals(LocalDate.of(1895, 12, 28));
    }
}