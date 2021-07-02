package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CatalogModel {
    private final int catalogID;
    private final String owner;
    private final boolean isAuctioned;
    private final BigDecimal basePrice;
    private final String description;
    private final List<CatalogItemModel> catalogItemModels;

    @Getter
    @Builder
    public static class CatalogItemModel {
        private final String photo;
        private final String description;
    }
}