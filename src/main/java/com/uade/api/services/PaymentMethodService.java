package com.uade.api.services;

import com.uade.api.entities.Client;
import com.uade.api.entities.PaymentMethod;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.PaymentMethodModel;
import com.uade.api.repositories.ClientRepository;
import com.uade.api.repositories.PaymentMethodRepository;
import com.uade.api.services.factory.PaymentMethodBuilder;
import com.uade.api.services.factory.PaymentMethodBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodService {

    private final PaymentMethodBuilderFactory paymentMethodBuilderFactory;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PaymentMethodBuilderFactory paymentMethodBuilderFactory) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodBuilderFactory = paymentMethodBuilderFactory;
    }


    public void addPaymentMethod(PaymentMethodModel model, Client client) {
        PaymentMethodBuilder paymentMethodBuilder = paymentMethodBuilderFactory.getPaymentMethodBuilder(model.getType());

        PaymentMethod paymentMethod = paymentMethodBuilder.build(model, client);

        paymentMethodRepository.save(paymentMethod);
    }
}
