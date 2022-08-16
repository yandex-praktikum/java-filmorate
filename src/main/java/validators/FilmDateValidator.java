package validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validators.annotations.CorrectFilmDate;

import java.time.LocalDate;

public class FilmDateValidator implements ConstraintValidator<CorrectFilmDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
