package com.uade.api.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class AuctionModel {
    private final Integer number;
    private final String image;
    private final Date date;
    private final Time time;
    private final int auctionOwner;
    private final String location;
    private final int attendeeCapacity;
    private final DepositStatus hasDeposit;
    private final CategoryType category;
    private final AuctionStatus status;
}