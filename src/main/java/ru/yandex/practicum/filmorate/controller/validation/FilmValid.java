package ru.yandex.practicum.filmorate.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmValidator.class)
public @interface FilmValid {
    String message() default "error film data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

