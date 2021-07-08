package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductModel {
    private int id;
    private List<String> images;
    private String description;
    private String fullDescription;

    private ProductStatus productStatus;
    private BigDecimal basePrice;
    private BigDecimal commission;
    private Date assignedDate;
    private Integer assignedAuction;
    private Date soldDate;
    private BigDecimal soldAmount;
    private BigDecimal earnings;
}
