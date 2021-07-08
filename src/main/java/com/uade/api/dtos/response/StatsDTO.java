package com.uade.api.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StatsDTO {
    public AuctionRatioDTO auctionRatio;
    public List<CategoryParticipationDTO> categoryParticipation;

    @Getter
    @Setter
    @Builder
    public static class AuctionRatioDTO {
        public long won;
        public long lost;
    }

    @Getter
    @Setter
    @Builder
    public static class CategoryParticipationDTO {
        public String category;
        public int value;
    }
}