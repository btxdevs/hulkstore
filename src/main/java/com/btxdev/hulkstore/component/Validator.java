package com.btxdev.hulkstore.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Log4j2
@Component
public class Validator {
    private final javax.validation.Validator javaxValidator;

    public Validator() {
        this.javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T> T validate(@NotNull T t){
        Set<ConstraintViolation<T>> violations = javaxValidator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return t;
    }
}
