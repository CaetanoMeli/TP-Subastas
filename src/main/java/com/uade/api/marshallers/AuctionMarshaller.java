package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionCatalogDTO;
import com.uade.api.dtos.response.AuctionDetailDTO;
import com.uade.api.entities.Catalog;
import com.uade.api.entities.Picture;
import com.uade.api.models.AuctionModel;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuctionMarshaller {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm'hs'";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public AuctionDetailDTO buildAuctionDetail(AuctionModel auction) {
        String firstCatalogDescription = auction.getCatalogList()
                .stream()
                .findFirst()
                .map(Catalog::getDescription)
                .orElse("");

        return AuctionDetailDTO.of(
                auction.getNumber(),
                String.format("Subasta #%s", auction.getNumber()),
                AuctionDetailDTO.DetailDTO.of(auction.getDate().format(DATE_FORMATTER), auction.getAuctionOwner(), auction.getCategory().value(), auction.getCatalogList().size(), firstCatalogDescription)
        );
    }

    public AuctionCatalogDTO buildAuctionCatalog(AuctionModel auction) {
        return AuctionCatalogDTO.of(
                auction.getNumber(),
                auction.getCatalogList().stream()
                    .map(this::modelToArticleDTO)
                    .collect(Collectors.toList()));
    }

    private AuctionCatalogDTO.ArticleDTO modelToArticleDTO(Catalog catalog) {
        return AuctionCatalogDTO.ArticleDTO.of(
                String.format("Catalogo #%s", catalog.getId()),
                "Subastandose",
                true,
                catalog.getDescription(),
                "Juan",
                "$100",
                catalog.getCatalogItems().stream()
                    .findFirst()
                        .map(catalogItem -> catalogItem.getProduct()
                                .getPictures()
                                .stream()
                                .map(Picture::getPhoto)
                                .map(AuctionCatalogDTO.PictureDTO::of)
                                .collect(Collectors.toList())
                        ).orElse(List.of(AuctionCatalogDTO.PictureDTO.of(""))));
    }

}
