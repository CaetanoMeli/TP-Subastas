package com.uade.api.services.factory;

import com.uade.api.exceptions.InternalServerException;
import com.uade.api.models.PaymentMethodType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentMethodBuilderFactory {
    private final Map<PaymentMethodType, PaymentMethodBuilder> BUILDER_BY_TYPE;

    public PaymentMethodBuilderFactory(final CreditCardMethodBuilder creditCardMethodBuilder, final BankAccountMethodBuilder bankAccountMethodBuilder) {
        BUILDER_BY_TYPE = Map.of(
                PaymentMethodType.CREDIT_CARD, creditCardMethodBuilder,
                PaymentMethodType.BANK_ACCOUNT, bankAccountMethodBuilder
        );
    }

    public PaymentMethodBuilder getPaymentMethodBuilder(PaymentMethodType type) {
        PaymentMethodBuilder builder = BUILDER_BY_TYPE.get(type);

        if (builder == null) {
            throw new InternalServerException();
        }

        return builder;
    }
}
