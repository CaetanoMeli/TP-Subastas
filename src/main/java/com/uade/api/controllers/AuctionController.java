package com.uade.api.controllers;

import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.marshallers.AuctionMarshaller;
import com.uade.api.models.AuctionModel;
import com.uade.api.services.AuctionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionMarshaller auctionMarshaller;

    public AuctionController(
            AuctionService auctionService,
            AuctionMarshaller auctionMarshaller
    ) {
        this.auctionService = auctionService;
        this.auctionMarshaller = auctionMarshaller;
    }

    @GetMapping(value = "/{id}")
    public AuctionDetailDTO getAuction(@PathVariable(value = "id") Integer auctionId) {
        AuctionModel auctionModel = auctionService.getAuction(auctionId);

        return auctionMarshaller.buildAuctionDetail(auctionModel);
    }
}
