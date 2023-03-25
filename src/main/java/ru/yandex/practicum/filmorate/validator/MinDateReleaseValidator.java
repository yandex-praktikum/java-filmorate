package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinDateReleaseValidator implements ConstraintValidator<MinDateRelease, LocalDate> {
    @Override
    public boolean isValid(LocalDate dateString, ConstraintValidatorContext context) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate minDateRelease = LocalDate.parse("1895-12-28", formatter);
        return !dateString.isBefore(minDateRelease);
    }
}
