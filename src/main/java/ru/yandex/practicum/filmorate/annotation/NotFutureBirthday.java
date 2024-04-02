package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.validator.NotFutureBirthdayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotFutureBirthdayValidator.class})
public @interface NotFutureBirthday {

    String message() default "{Дата рождения не может быть в будущем}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}