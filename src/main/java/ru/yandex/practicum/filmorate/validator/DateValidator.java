package ru.yandex.practicum.filmorate.validator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class DateValidator implements ConstraintValidator<DateValidationInterface, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && !value.isBefore(LocalDate.of(1895, Month.DECEMBER, 28));
    }

}
