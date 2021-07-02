package com.uade.api.marshallers;

import com.uade.api.dtos.response.PaymentMethodsDTO;
import com.uade.api.models.PaymentMethodModel;
import com.uade.api.models.PaymentMethodType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMethodMarshaller {

    public PaymentMethodsDTO buildPaymentMethods(List<PaymentMethodModel> paymentMethods) {
        return PaymentMethodsDTO.builder()
                .paymentMethods(paymentMethods.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList())
                ).build();
    }

    private PaymentMethodsDTO.PaymentMethodDTO mapToDTO(PaymentMethodModel model) {
        String number = model.getType().equals(PaymentMethodType.CREDIT_CARD) ?
                String.format("XXXX XXXX XXXX %s", model.getLastFourDigits()) :
                model.getNumber();

        String name = model.getType().equals(PaymentMethodType.CREDIT_CARD) ?
                String.format("Tarjeta %s", model.getCompany()) :
                String.format("Cuenta Corriente %s", model.getCompany());

        return PaymentMethodsDTO.PaymentMethodDTO.builder()
                .type(model.getType().value())
                .approved(model.isApproved())
                .company(model.getCompany())
                .id(model.getId())
                .number(number)
                .name(name)
                .build();
    }
}
