package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.validator.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ReleaseDateValidator.class})
public @interface NotReleaseDateBefore1895 {
    String message() default "{Дата релиза — не раньше 28 декабря 1895 года}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}