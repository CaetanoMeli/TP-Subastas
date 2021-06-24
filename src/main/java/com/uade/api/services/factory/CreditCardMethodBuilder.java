package com.uade.api.services.factory;

import com.uade.api.entities.Client;
import com.uade.api.entities.PaymentMethod;
import com.uade.api.models.PaymentMethodModel;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMethodBuilder implements PaymentMethodBuilder {
    @Override
    public PaymentMethod build(PaymentMethodModel model, Client client) {
        String lastFourDigits = model.getBin().substring(model.getBin().length() - 4);

        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setClient(client);
        paymentMethod.setType(model.getType().value());
        paymentMethod.setLastFourDigits(lastFourDigits);
        paymentMethod.setCompany(model.getCompany());


        return paymentMethod;
    }
}
