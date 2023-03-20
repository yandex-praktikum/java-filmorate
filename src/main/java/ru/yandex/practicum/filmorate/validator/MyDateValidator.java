package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateValidator implements ConstraintValidator<ValidDate, LocalDate> {
    public void initialize(ValidDate constraint) {
    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // validate the value here.
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDate start = LocalDate.parse("28-12-1895", f);
        boolean dateIsValid = value.isAfter(start) ? true : false;
        return dateIsValid;
    }
}