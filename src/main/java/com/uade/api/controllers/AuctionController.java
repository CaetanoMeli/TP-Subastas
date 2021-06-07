package com.uade.api.controllers;

import com.uade.api.dtos.response.HomeDTO;
import com.uade.api.marshallers.AuctionMarshaller;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.ClientModel;
import com.uade.api.services.AuctionService;
import com.uade.api.services.CatalogService;
import com.uade.api.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AuctionController {

    private final ClientService clientService;
    private final AuctionService auctionService;
    private final CatalogService catalogService;
    private final AuctionMarshaller auctionMarshaller;

    public AuctionController(
            ClientService clientService,
            AuctionService auctionService,
            CatalogService catalogService,
            AuctionMarshaller auctionMarshaller
    ) {
        this.clientService = clientService;
        this.auctionService = auctionService;
        this.catalogService = catalogService;
        this.auctionMarshaller = auctionMarshaller;
    }

    @GetMapping(value = "/validate")
    public HomeDTO getHome(@RequestParam(required = false) int userId) {
        ClientModel clientModel = clientService.getClient(userId);
        Map<Integer, AuctionModel> auctions = auctionService.getAuctions();
        List<CatalogModel> catalogs = catalogService.getCatalogs();

        return auctionMarshaller.buildHome(auctions, catalogs, clientModel.getCategoryType());
    }
}
