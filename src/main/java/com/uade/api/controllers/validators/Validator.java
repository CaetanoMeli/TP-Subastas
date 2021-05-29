package com.uade.api.controllers.validators;

import com.uade.api.exceptions.BadRequestException;
import org.apache.logging.log4j.util.Strings;

public interface Validator {

    default void nonEmptyString(String input) {
        if (Strings.isBlank(input)) {
            throw new BadRequestException();
        }
    }
}
