package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
public class AuctionModel {
    private final Integer number;
    private final String image;
    private final CurrencyType currencyType;
    private final ZonedDateTime date;
    private final String auctionOwner;
    private final String location;
    private final int attendeeCapacity;
    private final DepositStatus hasDeposit;
    private final CategoryType category;
    private final AuctionStatus status;
    private final List<CatalogModel> catalogList;
}