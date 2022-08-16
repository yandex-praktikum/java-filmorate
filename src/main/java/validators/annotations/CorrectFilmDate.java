package validators.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import validators.FilmDateValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = FilmDateValidator.class)
@Documented
public @interface CorrectFilmDate {

    String message() default "{FilmDate.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}