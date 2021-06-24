package com.uade.api.controllers.validators;

import com.uade.api.dtos.request.NewPaymentMethodDTO;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.models.PaymentMethodType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class PaymentTypeValidator implements Validator {
    private final Map<String, PaymentValidator<NewPaymentMethodDTO>> VALIDATOR_PER_TYPE;

    public PaymentTypeValidator(final CreditCardValidator creditCardValidator, final BankAccountValidator bankAccountValidator) {
        VALIDATOR_PER_TYPE = Map.of(
                PaymentMethodType.CREDIT_CARD.value(), creditCardValidator,
                PaymentMethodType.BANK_ACCOUNT.value(), bankAccountValidator
        );
    }

    public void validateNewPaymentMethod(NewPaymentMethodDTO newPaymentMethodDTO) {
        nonEmptyString(newPaymentMethodDTO.type);

        Optional.ofNullable(VALIDATOR_PER_TYPE.get(newPaymentMethodDTO.type))
            .ifPresentOrElse(validator -> validator.validate(newPaymentMethodDTO), () -> {
                throw new BadRequestException();
            });
    }
}
