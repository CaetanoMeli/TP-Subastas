package com.uade.api.services.factory;

import com.uade.api.entities.Client;
import com.uade.api.entities.PaymentMethod;
import com.uade.api.models.PaymentMethodModel;

public interface PaymentMethodBuilder {
    PaymentMethod build(PaymentMethodModel model, Client client);
}
