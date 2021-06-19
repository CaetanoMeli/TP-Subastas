package com.uade.api.controllers;

import com.uade.api.dtos.response.HomeDTO;
import com.uade.api.marshallers.HomeMarshaller;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.UserModel;
import com.uade.api.services.AuctionService;
import com.uade.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auctions")
public class AuctionController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final HomeMarshaller homeMarshaller;

    public AuctionController(
            UserService userService,
            AuctionService auctionService,
            HomeMarshaller homeMarshaller
    ) {
        this.userService = userService;
        this.auctionService = auctionService;
        this.homeMarshaller = homeMarshaller;
    }

    @GetMapping(value = "/{id}")
    public AuctionModel getAuction(@PathVariable(value = "id") Integer auctionId) {
        return auctionService.getAuction(auctionId);
    }
}
