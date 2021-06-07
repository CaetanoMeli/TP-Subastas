package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionDTO;
import com.uade.api.dtos.response.FilterDTO;
import com.uade.api.dtos.response.HomeDTO;
import com.uade.api.dtos.response.MenuDTO;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.CatalogModel;
import com.uade.api.models.CategoryType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuctionMarshaller {

    public HomeDTO buildHome(Map<Integer, AuctionModel> auctions, List<CatalogModel> catalogs, CategoryType userCategory) {
        return HomeDTO.of(
                MenuDTO.of("Inicio", "home"),
                List.of(
                    FilterDTO.of(
                        "Categorias",
                        List.of(FilterDTO.FilterType.of("comun"))
                    )
                ),
                buildAuctions(auctions, catalogs, userCategory)
        );
    }

    private List<AuctionDTO> buildAuctions(Map<Integer, AuctionModel> auctions, List<CatalogModel> catalogs, CategoryType userCategory) {
        List<AuctionDTO> auctionDTOS;
        if (userCategory != null) {
            auctionDTOS = buildAuctionsForLoggedUser(auctions, catalogs, userCategory);
        } else {
            auctionDTOS = buildAuctionsForUnloggedUser(auctions, catalogs);
        }

        return auctionDTOS;
    }

    private List<AuctionDTO> buildAuctionsForUnloggedUser(Map<Integer, AuctionModel> auctions, List<CatalogModel> catalogs) {
        return catalogs
                .stream()
                .sorted(catalogCategoryComparator(auctions).reversed())
                .map(catalog -> modelToDTO(auctions.get(catalog.getAuctionID()), catalog))
                .collect(Collectors.toList());
    }

    private List<AuctionDTO> buildAuctionsForLoggedUser(Map<Integer, AuctionModel> auctions, List<CatalogModel> catalogs, CategoryType userCategory) {
        List<AuctionDTO> userCategoryCatalogs = catalogs
                .stream()
                .filter(catalog -> userCategory.equals(auctions.get(catalog.getAuctionID()).getCategory()))
                .map(catalog -> modelToDTO(auctions.get(catalog.getAuctionID()), catalog))
                .collect(Collectors.toList());

        List<AuctionDTO> prioritySortedCatalogs = catalogs
                .stream()
                .filter(catalog -> !userCategory.equals(auctions.get(catalog.getAuctionID()).getCategory()))
                .sorted(catalogCategoryComparator(auctions))
                .map(catalog -> modelToDTO(auctions.get(catalog.getAuctionID()), catalog))
                .collect(Collectors.toList());

        userCategoryCatalogs.addAll(prioritySortedCatalogs);

        return userCategoryCatalogs;
    }

    private Comparator<CatalogModel> catalogCategoryComparator(Map<Integer, AuctionModel> auctions) {
        return Comparator.comparingInt(c -> auctions.get(c.getAuctionID()).getCategory().priority());
    }

    private AuctionDTO modelToDTO(AuctionModel auctionModel, CatalogModel catalogModel) {
        return AuctionDTO.of(
            auctionModel.getNumber(),
            catalogModel.getDescription(),
            auctionModel.getCategory().value(),
            auctionModel.getStatus().value()
        );
    }

}
