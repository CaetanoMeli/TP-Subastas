package com.uade.api.services;

import com.uade.api.entities.Auction;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.AuctionStatus;
import com.uade.api.models.CategoryType;
import com.uade.api.models.DepositStatus;
import com.uade.api.repositories.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Map<Integer, AuctionModel> getAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        Map<Integer, AuctionModel> model = null;

        if (auctions != null && auctions.size() > 0) {
            model = auctions.stream()
                    .map(auction -> AuctionModel.of(
                            auction.getId(),
                            auction.getImage(),
                            auction.getDate(),
                            auction.getTime(),
                            auction.getAuctioner(),
                            auction.getLocation(),
                            auction.getCapacity(),
                            DepositStatus.fromString(auction.getHasDeposit()),
                            CategoryType.fromString(auction.getCategory()),
                            AuctionStatus.fromString(auction.getStatus())
                            )
                    )
                    .collect(Collectors.toMap(AuctionModel::getNumber, auction -> auction));
        }

        return model;
    }
}
