package com.uade.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StatsModel {
    public AuctionRatioModel auctionRatio;
    public List<CategoryParticipationModel> categoryParticipation;

    @Getter
    @Setter
    @Builder
    public static class AuctionRatioModel {
        public long won;
        public long lost;
    }

    @Getter
    @Setter
    @Builder
    public static class CategoryParticipationModel {
        public String category;
        public int value;
    }
}