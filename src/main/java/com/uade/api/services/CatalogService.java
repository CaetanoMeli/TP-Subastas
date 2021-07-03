package com.uade.api.services;

import com.uade.api.entities.Auction;
import com.uade.api.entities.Bid;
import com.uade.api.entities.Catalog;
import com.uade.api.entities.CatalogItem;
import com.uade.api.entities.Owner;
import com.uade.api.entities.Picture;
import com.uade.api.entities.User;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.AuctionStatus;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.CategoryType;
import com.uade.api.models.CurrencyType;
import com.uade.api.models.DepositStatus;
import com.uade.api.repositories.AuctionRepository;
import com.uade.api.repositories.CatalogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public CatalogModel getCatalog(Integer id) {
        Optional<Catalog> catalogModel = catalogRepository.findById(id);

        return catalogModel
                .map(this::mapToModel)
                .orElseThrow(NotFoundException::new);
    }

    public CatalogModel mapToModel(Catalog catalog) {
        List<CatalogItem> catalogItems = catalog.getCatalogItems();

        User owner = getOwner(catalog).getUser();

        BigDecimal basePrice = catalogItems.stream()
                .map(CatalogItem::getBasePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CatalogModel.CatalogItemModel> catalogItemModels = catalogItems.stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());

        Bid winningBid = catalog.getBids().stream()
                .max(Comparator.comparing(Bid::getAmount))
                .orElse(null);

        String auctionCategory = catalog.getAuction().getCategory();

        return CatalogModel.builder()
                .catalogID(catalog.getId())
                .description(catalog.getDescription())
                .catalogItemModels(catalogItemModels)
                .winningBid(winningBid)
                .categoryType(CategoryType.fromString(auctionCategory))
                .owner(owner)
                .entity(catalog)
                .basePrice(basePrice)
                .isAuctioned(isAuctioned(catalogItems))
                .build();
    }

    public boolean isAuctioned(Catalog catalog) {
        return isAuctioned(catalog.getCatalogItems());
    }

    public Owner getOwner(Catalog catalog) {
        return catalog.getCatalogItems()
                .stream()
                .findFirst()
                .map(item -> item.getProduct().getOwner())
                .orElseThrow(NotFoundException::new);
    }

    public BigDecimal getComission(Catalog catalog) {
        return catalog.getCatalogItems()
                .stream()
                .map(CatalogItem::getComission)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CatalogModel.CatalogItemModel mapToModel(CatalogItem catalogItem) {
        return CatalogModel.CatalogItemModel.builder()
                .photo(catalogItem.getProduct().getPictures().stream()
                        .findFirst()
                        .map(Picture::getPhoto)
                        .orElse(""))
                .description(catalogItem.getProduct().getCatalogDescription())
                .build();
    }

    private boolean isAuctioned(List<CatalogItem> catalogItems) {
        return catalogItems.stream()
                .allMatch(this::isAuctioned);
    }

    private boolean isAuctioned(CatalogItem catalogItem) {
        final String IS_AUCTIONED = "si";

        return IS_AUCTIONED.equals(catalogItem.getAuctioned());
    }

}
