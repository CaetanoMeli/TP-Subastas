package com.uade.api.models;

import com.uade.api.entities.Bid;
import com.uade.api.entities.Catalog;
import com.uade.api.entities.User;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CatalogModel {
    private final int catalogID;
    private final User owner;
    private final boolean isAuctioned;
    private final BigDecimal basePrice;
    private final String description;
    private final CategoryType categoryType;
    private final List<CatalogItemModel> catalogItemModels;
    private final Bid winningBid;
    private final Catalog entity;

    @Getter
    @Builder
    public static class CatalogItemModel {
        private final String photo;
        private final String description;
    }
}