package com.uade.api.services;

import com.uade.api.entities.Auction;
import com.uade.api.entities.Catalog;
import com.uade.api.entities.User;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.AuctionStatus;
import com.uade.api.models.CategoryType;
import com.uade.api.models.DepositStatus;
import com.uade.api.repositories.AuctionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public AuctionModel getAuction(Integer id) {
        Optional<Auction> auctionOptional = auctionRepository.findById(id);

        return auctionOptional
                .map(this::mapToExtendedModel)
                .orElseThrow(NotFoundException::new);
    }

    public List<AuctionModel> getAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        List<AuctionModel> model = null;

        if (auctions != null && auctions.size() > 0) {
            model = auctions.stream()
                    .map(this::mapToModel)
                    .collect(Collectors.toList());
        }

        return model;
    }

    private AuctionModel mapToModel(Auction auction) {
        return AuctionModel.builder()
                .number(auction.getId())
                .image(auction.getImage())
                .date(getAuctionDate(auction))
                .location(auction.getLocation())
                .attendeeCapacity(auction.getCapacity())
                .hasDeposit(DepositStatus.fromString(auction.getHasDeposit()))
                .category(CategoryType.fromString(auction.getCategory()))
                .status(AuctionStatus.fromString(auction.getStatus()))
                .build();
    }

    private AuctionModel mapToExtendedModel(Auction auction) {
        User auctioner = auction.getAuctioner().getUser();
        String auctionerName = auctioner.getFirstName() + " " + auctioner.getLastName();

        List<Catalog> catalogs = auction.getCatalogList();

        return AuctionModel.builder()
                .number(auction.getId())
                .image(auction.getImage())
                .date(getAuctionDate(auction))
                .auctionOwner(auctionerName)
                .location(auction.getLocation())
                .attendeeCapacity(auction.getCapacity())
                .hasDeposit(DepositStatus.fromString(auction.getHasDeposit()))
                .category(CategoryType.fromString(auction.getCategory()))
                .status(AuctionStatus.fromString(auction.getStatus()))
                .catalogList(catalogs)
                .build();
    }

    private ZonedDateTime getAuctionDate(Auction auction) {
        ZonedDateTime date = ZonedDateTime.ofInstant(auction.getDate().toInstant(), ZoneId.systemDefault());

        ZonedDateTime dateTime = ZonedDateTime.ofInstant(LocalDate.EPOCH
                .atTime(auction.getTime().toLocalTime())
                .atZone(ZoneId.systemDefault())
                .toInstant(), ZoneId.systemDefault());

        date = date.plusHours(dateTime.getHour());
        date = date.plusMinutes(dateTime.getMinute());
        date = date.plusSeconds(dateTime.getSecond());
        date = date.plusNanos(dateTime.getNano());

        return date;
    }

}
