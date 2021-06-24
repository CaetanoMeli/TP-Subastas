package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentMethodModel {
    private int id;
    private String bin;
    private String number;
    private String company;
    private boolean approved;
    private PaymentMethodType type;
    private String lastFourDigits;
}
