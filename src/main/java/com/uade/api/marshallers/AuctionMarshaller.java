package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.entities.Catalog;
import com.uade.api.models.AuctionModel;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AuctionMarshaller {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm'hs'";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public AuctionDetailDTO buildAuctionDetail(AuctionModel auction) {
        String firstCatalogDescription = auction.getCatalogList()
                .stream()
                .findFirst()
                .map(Catalog::getDescription)
                .orElse("");

        return AuctionDetailDTO.of(
                auction.getNumber(),
                String.format("Subasta #%s", auction.getNumber()),
                AuctionDetailDTO.DetailDTO.of(auction.getDate().format(DATE_FORMATTER), auction.getAuctionOwner(), auction.getCategory().value(), auction.getCatalogList().size(), firstCatalogDescription)
        );
    }

}
