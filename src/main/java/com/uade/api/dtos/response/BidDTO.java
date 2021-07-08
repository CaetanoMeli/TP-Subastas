package com.uade.api.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
public class BidDTO {
    private int id;
    private int catalogId;
    private BigDecimal amount;
    private String result;
    private String createdDate;
}