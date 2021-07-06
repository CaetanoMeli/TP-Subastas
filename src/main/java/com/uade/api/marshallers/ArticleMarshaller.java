package com.uade.api.marshallers;

import com.uade.api.dtos.response.ProductDTO;
import com.uade.api.models.ProductModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMarshaller {

    public List<ProductDTO> buildArticles(List<ProductModel> products) {
        return products.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToDTO(ProductModel model) {
        return ProductDTO.builder()
                .images(model.getImages())
                .soldDate(model.getSoldDate())
                .earnings(model.getEarnings())
                .basePrice(model.getBasePrice())
                .commission(model.getCommission())
                .soldAmount(model.getSoldAmount())
                .description(model.getDescription())
                .assignedDate(model.getAssignedDate())
                .assignedAuction(model.getAssignedAuction())
                .fullDescription(model.getFullDescription())
                .productStatus(model.getProductStatus().value())
                .build();
    }
}
