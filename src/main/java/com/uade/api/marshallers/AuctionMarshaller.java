package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionCatalogDTO;
import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.UserModel;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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
                auction.getCatalogList().stream()
                    .map(catalog -> modelToArticleDTO(auction, catalog, userModel))
                    .collect(Collectors.toList()));
    }

    private AuctionCatalogDTO.ArticleDTO modelToArticleDTO(AuctionModel auction, CatalogModel catalog, UserModel userModel) {
        String auctionStatus = catalog.isAuctioned() ? "Subastado" : "Subastandose";
        boolean userSameCategoryAsAuction = userModel != null && auction.getCategory().equals(userModel.getCategory());
        boolean userIsVerified = userModel != null && ClientStatus.ADMITTED.equals(userModel.getClientStatus());

        return AuctionCatalogDTO.ArticleDTO.of(
                String.format("Catalogo #%s", catalog.getCatalogID()),
                auctionStatus,
                userIsVerified && !catalog.isAuctioned() && userSameCategoryAsAuction,
                catalog.getDescription(),
                catalog.getOwner(),
                auction.getCurrencyType().currencyId() + catalog.getBasePrice(),
                catalog.getCatalogItemModels().stream()
                    .map(CatalogModel.CatalogItemModel::getPhoto)
                    .map(AuctionCatalogDTO.PictureDTO::of)
                    .collect(Collectors.toList()));
    }

}
