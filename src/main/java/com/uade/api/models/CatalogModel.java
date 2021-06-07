package com.uade.api.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class CatalogModel {
    private final int catalogID;
    private final int auctionID;
    private final int catalogOwner;
    private final String description;
}