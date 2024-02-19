package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeMinValidator.class)
public @interface DateTimeMin {
    String message() default "Date should not be early than {date}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String date();
}
