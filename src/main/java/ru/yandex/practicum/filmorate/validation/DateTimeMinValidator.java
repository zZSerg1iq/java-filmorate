package ru.yandex.practicum.filmorate.validation;

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
        return (value != null && (value.isEqual(earliestAllowedDate) || value.isAfter(earliestAllowedDate)));
    }
}