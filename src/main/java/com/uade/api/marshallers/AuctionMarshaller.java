package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionCatalogDTO;
import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.AuctionStatus;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.CatalogStatus;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.UserModel;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class AuctionMarshaller {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm'hs'";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public AuctionDetailDTO buildAuctionDetail(AuctionModel auction) {
        String firstCatalogDescription = auction.getCatalogList()
                .stream()
                .findFirst()
                .map(CatalogModel::getDescription)
                .orElse("");

        return AuctionDetailDTO.of(
                auction.getNumber(),
                String.format("Subasta #%s", auction.getNumber()),
                AuctionDetailDTO.DetailDTO.of(auction.getDate().format(DATE_FORMATTER), auction.getAuctionOwner(), auction.getCategory().value(), auction.getCatalogList().size(), firstCatalogDescription)
        );
    }

    public AuctionCatalogDTO buildAuctionCatalog(AuctionModel auction, UserModel userModel) {
        return AuctionCatalogDTO.of(
                auction.getNumber(),
                IntStream
                        .range(0, auction.getCatalogList().size())
                        .mapToObj(i -> modelToArticleDTO(auction, auction.getCatalogList().get(i), i > 0 ? auction.getCatalogList().get(i - 1) : null, userModel))
                        .collect(Collectors.toList()));
    }

    private AuctionCatalogDTO.ArticleDTO modelToArticleDTO(AuctionModel auction, CatalogModel currentCatalog, CatalogModel previousCatalog, UserModel userModel) {
        ZonedDateTime now = ZonedDateTime.now();
        boolean isFutureAuction = auction.getDate().isAfter(now);
        Function<CatalogModel, String> getStatus = catalog -> isFutureAuction ? CatalogStatus.TO_AUCTION.value() : catalog.isAuctioned() ? CatalogStatus.AUCTIONED.value() : CatalogStatus.AUCTIONING.value();

        String catalogStatus;

        if (previousCatalog != null && List.of(CatalogStatus.AUCTIONING.value(), CatalogStatus.TO_AUCTION.value()).contains(getStatus.apply(previousCatalog))) {
            catalogStatus = CatalogStatus.TO_AUCTION.value();
        } else {
            catalogStatus = getStatus.apply(currentCatalog);
        }


        String ownerName = currentCatalog.getOwner().getFirstName() + " " + currentCatalog.getOwner().getLastName();
        boolean userSameAsOwner = userModel != null && userModel.getId().equals(currentCatalog.getOwner().getId());
        boolean userSameCategoryAsAuction = userModel != null && auction.getCategory().priority() >= userModel.getCategory().priority();
        boolean userIsVerified = userModel != null && ClientStatus.ADMITTED.equals(userModel.getClientStatus());

        return AuctionCatalogDTO.ArticleDTO.of(
                String.format("Catalogo #%s", currentCatalog.getCatalogID()),
                catalogStatus,
                currentCatalog.getCatalogID(),
                userIsVerified && !userSameAsOwner && !userModel.isHasActiveBid() && !currentCatalog.isAuctioned() && userSameCategoryAsAuction && CatalogStatus.AUCTIONING.value().equals(catalogStatus),
                currentCatalog.getDescription(),
                currentCatalog.getCatalogItemModels().stream()
                        .map(CatalogModel.CatalogItemModel::getDescription)
                        .collect(Collectors.joining("\n\n")),
                ownerName,
                auction.getCurrencyType().currencyId() + currentCatalog.getBasePrice(),
                currentCatalog.getCatalogItemModels().stream()
                    .map(CatalogModel.CatalogItemModel::getPhoto)
                    .map(AuctionCatalogDTO.PictureDTO::of)
                    .collect(Collectors.toList()));
    }

}
