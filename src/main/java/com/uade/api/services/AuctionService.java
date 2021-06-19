package com.uade.api.services;

import com.uade.api.entities.Auction;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
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
        ZonedDateTime date = ZonedDateTime.ofInstant(auction.getDate().toInstant(), ZoneId.systemDefault());

        ZonedDateTime dateTime = ZonedDateTime.ofInstant(LocalDate.EPOCH
                .atTime(auction.getTime().toLocalTime())
                .atZone(ZoneId.systemDefault())
                .toInstant(), ZoneId.systemDefault());

        date.plusHours(dateTime.getHour());
        date.plusMinutes(dateTime.getMinute());
        date.plusSeconds(dateTime.getSecond());
        date.plusNanos(dateTime.getNano());

        return AuctionModel.of(
                auction.getId(),
                auction.getImage(),
                date,
                auction.getTime(),
                auction.getAuctioner(),
                auction.getLocation(),
                auction.getCapacity(),
                DepositStatus.fromString(auction.getHasDeposit()),
                CategoryType.fromString(auction.getCategory()),
                AuctionStatus.fromString(auction.getStatus())
        );
    }
}
