package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateTimeMinValidator implements ConstraintValidator<DateTimeMin, LocalDate> {

    private LocalDate earliestAllowedDate;

    @Override
    public void initialize(DateTimeMin constraintAnnotation) {
        this.earliestAllowedDate = LocalDate.parse(constraintAnnotation.date());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            throw new ValidationException("Дата релиза не может быть пустой");
        } else if (value.isBefore(earliestAllowedDate)) {
            throw new ValidationException("Слишком ранняя дата релиза");
        }
        return true;
    }
}