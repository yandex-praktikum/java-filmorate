package ru.yandex.practicum.filmorate.validate.annotation;

import ru.yandex.practicum.filmorate.validate.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseValidation {

    String message() default "Ошибка валидации даты фильма. Дата должна быть не раньше 28.12.1895";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}