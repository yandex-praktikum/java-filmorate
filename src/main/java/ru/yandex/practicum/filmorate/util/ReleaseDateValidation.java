package ru.yandex.practicum.filmorate.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDateValidation {
    String message() default "дата релиза — раньше 28 декабря 1895 года";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
