package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyDateValidator.class)
@Documented
public @interface ValidDate {

    String message() default "Дата релиза должна быть не раньше 28 декабря 1895 года;";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

