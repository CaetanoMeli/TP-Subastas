package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class BidModel {
    private int id;
    private int catalogId;
    private BigDecimal amount;
    private String result;
    private ZonedDateTime createdDate;
}