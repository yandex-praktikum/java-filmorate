package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MinDateReleaseValidator.class)
@Documented
public @interface MinDateRelease {
    String message() default "Дата выхода фильма не должна быть раннее 28 декабря 1895г.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
