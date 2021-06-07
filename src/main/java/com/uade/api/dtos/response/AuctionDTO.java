package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class AuctionDTO {
    public int number;
    public String title;
    public String category;
    public String status;
}
