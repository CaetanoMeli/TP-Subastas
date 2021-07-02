package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class AuctionCatalogDTO {
    public Integer auctionId;
    public List<ArticleDTO> articles;

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class ArticleDTO {
        public String title;
        public String status;
        public boolean canBid;
        public String description;
        public String catalogDescription;
        public String owner;
        public String basePrice;
        public List<PictureDTO> pictures;
    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class PictureDTO {
        public String url;
    }
}