package com.uade.api.marshallers;

import com.uade.api.dtos.response.AuctionDTO;
import com.uade.api.dtos.response.FilterDTO;
import com.uade.api.dtos.response.HomeDTO;
import com.uade.api.dtos.response.MenuDTO;
import com.uade.api.models.AuctionModel;
import com.uade.api.models.CategoryType;
import com.uade.api.models.UserModel;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuctionMarshaller {

    public HomeDTO buildHome(List<AuctionModel> auctions, UserModel userModel) {
        return HomeDTO.of(
                buildMenuDTO(userModel),
                List.of(
                    FilterDTO.of(
                        "Categorias",
                        List.of(FilterDTO.FilterType.of("comun"))
                    )
                ),
                buildAuctions(auctions, userModel.getCategory())
        );
    }

    private MenuDTO buildMenuDTO(UserModel userModel) {
        MenuDTO dto = null;
        if (userModel != null) {
            dto = MenuDTO.of(userModel.getFirstName(), userModel.getLastName(), userModel.getCategory().value());
        }

        return dto;
    }

    private List<AuctionDTO> buildAuctions(List<AuctionModel> auctions, CategoryType userCategory) {
        List<AuctionDTO> auctionDTOS;
        if (userCategory != null) {
            auctionDTOS = buildAuctionsForLoggedUser(auctions, userCategory);
        } else {
            auctionDTOS = buildAuctionsForUnloggedUser(auctions);
        }

        return auctionDTOS;
    }

    private List<AuctionDTO> buildAuctionsForUnloggedUser(List<AuctionModel> auctions) {
        return auctions
                .stream()
                .sorted(catalogCategoryComparator().reversed())
                .map(this::modelToDTO)
                .collect(Collectors.toList());
    }

    private List<AuctionDTO> buildAuctionsForLoggedUser(List<AuctionModel> auctions, CategoryType userCategory) {
        List<AuctionDTO> userCategoryCatalogs = auctions
                .stream()
                .filter(auction -> userCategory.equals(auction.getCategory()))
                .map(this::modelToDTO)
                .collect(Collectors.toList());

        List<AuctionDTO> prioritySortedCatalogs = auctions
                .stream()
                .filter(auction -> !userCategory.equals(auction.getCategory()))
                .sorted(catalogCategoryComparator())
                .map(this::modelToDTO)
                .collect(Collectors.toList());

        userCategoryCatalogs.addAll(prioritySortedCatalogs);

        return userCategoryCatalogs;
    }

    private Comparator<AuctionModel> catalogCategoryComparator() {
        return Comparator.comparingInt(auction -> auction.getCategory().priority());
    }

    private AuctionDTO modelToDTO(AuctionModel auctionModel) {
        return AuctionDTO.of(
            auctionModel.getNumber(),
            String.format("Subasta #%s", auctionModel.getNumber()),
            auctionModel.getCategory().value(),
            auctionModel.getStatus().value(),
            auctionModel.getImage()
        );
    }

}
