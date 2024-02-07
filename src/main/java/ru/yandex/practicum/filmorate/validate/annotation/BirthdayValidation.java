package ru.yandex.practicum.filmorate.validate.annotation;

import ru.yandex.practicum.filmorate.validate.BirthdayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthdayValidator.class)
public @interface BirthdayValidation {

    String message() default "Ошибка валидации даты рождения. Дата рождения не может быть в будущем!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
