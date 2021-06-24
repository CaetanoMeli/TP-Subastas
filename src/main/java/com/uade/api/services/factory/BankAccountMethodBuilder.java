package com.uade.api.services.factory;

import com.uade.api.entities.Client;
import com.uade.api.entities.PaymentMethod;
import com.uade.api.models.PaymentMethodModel;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMethodBuilder implements PaymentMethodBuilder {
    @Override
    public PaymentMethod build(PaymentMethodModel model, Client client) {
        return PaymentMethod.builder()
                .client(client)
                .type(model.getType().value())
                .accountNumber(model.getNumber())
                .company(model.getCompany())
                .build();
    }
}
