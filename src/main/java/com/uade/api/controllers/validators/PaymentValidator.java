package com.uade.api.controllers.validators;

import com.uade.api.exceptions.BadRequestException;
import org.apache.logging.log4j.util.Strings;

public interface PaymentValidator<T> extends Validator {

    void validate(T requestBody);
}
