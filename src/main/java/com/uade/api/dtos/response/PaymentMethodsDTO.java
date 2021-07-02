package com.uade.api.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaymentMethodsDTO {
    private List<PaymentMethodDTO> paymentMethods;

    @Getter
    @Setter
    @Builder
    public static class PaymentMethodDTO {
        private int id;
        private String type;
        private String company;
        private String name;
        private String number;
        private boolean approved;
    }
}