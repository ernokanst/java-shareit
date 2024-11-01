package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateRangeValidator.class)

public @interface DateRange {
    String message() default "Дата начала должна быть раньше даты конца";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String before();
    String after();
}