package com.uade.api.controllers;

import com.uade.api.dtos.response.HomeDTO;
import com.uade.api.marshallers.AuctionMarshaller;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.UserModel;
import com.uade.api.services.AuctionService;
import com.uade.api.services.CatalogService;
import com.uade.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuctionController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final CatalogService catalogService;
    private final AuctionMarshaller auctionMarshaller;

    public AuctionController(
            UserService userService,
            AuctionService auctionService,
            CatalogService catalogService,
            AuctionMarshaller auctionMarshaller
    ) {
        this.userService = userService;
        this.auctionService = auctionService;
        this.catalogService = catalogService;
        this.auctionMarshaller = auctionMarshaller;
    }

    @GetMapping(value = "/home")
    public HomeDTO getHome(@RequestParam(required = false) Integer userId) {
        UserModel userModel = userService.getUser(userId);
        List<AuctionModel> auctions = auctionService.getAuctions();

        return auctionMarshaller.buildHome(auctions, userModel);
    }
}
