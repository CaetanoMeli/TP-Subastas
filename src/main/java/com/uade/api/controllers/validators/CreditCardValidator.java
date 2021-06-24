package com.uade.api.controllers.validators;

import com.uade.api.dtos.request.NewPaymentMethodDTO;
import org.springframework.stereotype.Component;

@Component
public class CreditCardValidator implements PaymentValidator<NewPaymentMethodDTO> {
    @Override
    public void validate(NewPaymentMethodDTO requestBody) {
        nonEmptyString(requestBody.bin);
        nonEmptyString(requestBody.company);
    }
}
