package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class AuctionDetailDTO {
    public Integer id;
    public String title;
    public DetailDTO detail;

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class DetailDTO {
        public String startDate;
        public String owner;
        public String category;
        public Integer articleAmount;
        public String description;
    }
}
