package com.uade.api.controllers;

import com.uade.api.dtos.response.AuctionCatalogDTO;
import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.marshallers.AuctionMarshaller;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.UserModel;
import com.uade.api.services.AuctionService;
import com.uade.api.services.BidService;
import com.uade.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auctions")
public class AuctionController {

    private final BidService bidService;
    private final UserService userService;
    private final AuctionService auctionService;
    private final AuctionMarshaller auctionMarshaller;

    public AuctionController(
            BidService bidService,
            UserService userService,
            AuctionService auctionService,
            AuctionMarshaller auctionMarshaller
    ) {
        this.bidService = bidService;
        this.userService = userService;
        this.auctionService = auctionService;
        this.auctionMarshaller = auctionMarshaller;
    }

    @GetMapping(value = "/{id}")
    public AuctionDetailDTO getAuction(@PathVariable(value = "id") Integer auctionId) {
        AuctionModel auctionModel = auctionService.getAuction(auctionId);

        return auctionMarshaller.buildAuctionDetail(auctionModel);
    }

    @GetMapping(value = "/{id}/catalog")
    public AuctionCatalogDTO getAuctionCatalog(@PathVariable(value = "id") Integer auctionId, @RequestParam(required = false) Integer userId) {
        AuctionModel auctionModel = auctionService.getAuction(auctionId);
        UserModel userModel = null;

        if (userId != null) {
            userModel = userService.getUser(userId, true);
        }

        return auctionMarshaller.buildAuctionCatalog(auctionModel, userModel);
    }
}
