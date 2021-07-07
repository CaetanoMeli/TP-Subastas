package com.uade.api.services;

import com.uade.api.entities.Bid;
import com.uade.api.entities.BidConfig;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.CategoryType;
import com.uade.api.models.TimeUnit;
import com.uade.api.models.UserModel;
import com.uade.api.repositories.BidConfigRepository;
import com.uade.api.repositories.BidRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class BidService {
    private static final int CONFIG_ID = 1;
    private static final int DEFAULT_MAX_TIME = 1;
    private static final String DEFAULT_UNIT = TimeUnit.MINUTES.value();

    private final BidRepository bidRepository;
    private final BidConfigRepository bidConfigRepository;
    private final CatalogService catalogService;

    public BidService(BidRepository bidRepository, BidConfigRepository bidConfigRepository, CatalogService catalogService) {
        this.bidRepository = bidRepository;
        this.bidConfigRepository = bidConfigRepository;
        this.catalogService = catalogService;
    }

    public void addBid(UserModel userModel, BigDecimal amount, Integer catalogId) {
        CatalogModel catalogModel = catalogService.getCatalog(catalogId);

        if (userModel.getId() == catalogModel.getOwner().getId()) {
            throw new BadRequestException("user_is_owner");
        }

        if (catalogModel.isAuctioned()) {
            throw new BadRequestException("already_auctioned");
        }

        boolean userHasActiveBid = userModel.getEntity().getClient().getBids()
                .stream()
                .filter(bid -> !catalogId.equals(bid.getCatalog().getId()))
                .anyMatch(bid -> !catalogService.isAuctioned(bid.getCatalog()));

        if (userHasActiveBid) {
            throw new BadRequestException("user_has_active_bid");
        }

        if (maxTimeElapsed(catalogModel)) {
            throw new BadRequestException("max_time_elapsed");
        }

        Bid winningBid = catalogModel.getWinningBid();

        if (winningBid != null && winningBid.getOwner().getId() == userModel.getId()) {
            throw new BadRequestException("user_already_has_winning_bid");
        }

        BigDecimal onePercentOfBasePrice = catalogModel.getBasePrice().movePointLeft(2);
        BigDecimal currentValue = winningBid != null  ? catalogModel.getBasePrice().add(winningBid.getAmount()) : null;
        BigDecimal twentyPercentOfLastWinningBid = currentValue != null ? currentValue.movePointLeft(1).multiply(new BigDecimal(2)) : null;

        boolean isHigherThanWinningBid = winningBid == null || amount.compareTo(winningBid.getAmount()) > 0;

        boolean isGoldOrPlatinum = CategoryType.GOLD.priority() >= catalogModel.getCategoryType().priority();

        boolean isValidAmount = amount.compareTo(onePercentOfBasePrice) >= 0 && isHigherThanWinningBid && (isGoldOrPlatinum || twentyPercentOfLastWinningBid != null && amount.compareTo(twentyPercentOfLastWinningBid) <= 0);

        if (!isValidAmount) {
            throw new BadRequestException("invalid_amount");
        }

        Bid bid = new Bid();
        bid.setOwner(catalogService.getOwner(catalogModel.getEntity()));
        bid.setClient(userModel.getEntity().getClient());
        bid.setAmount(amount);
        bid.setDateCreated(new Date());
        bid.setCatalog(catalogModel.getEntity());
        bid.setComission(catalogService.getComission(catalogModel.getEntity()));

        bidRepository.save(bid);
    }

    public boolean maxTimeElapsed(CatalogModel catalogModel) {
        BidConfig bidConfig = bidConfigRepository.findById(CONFIG_ID)
                .orElseGet(() -> {
                    BidConfig config = new BidConfig();
                    config.setMaxTimeAfterLastBid(DEFAULT_MAX_TIME);
                    config.setUnit(DEFAULT_UNIT);

                    return config;
                });

        ChronoUnit unit = TimeUnit.fromString(bidConfig.getUnit()).chronoUnit();

        Bid winningBid = catalogModel.getWinningBid();

        return winningBid != null && unit.between(catalogModel.getWinningBid().getDateCreated().toInstant(), new Date().toInstant()) < bidConfig.getMaxTimeAfterLastBid();
    }
}
