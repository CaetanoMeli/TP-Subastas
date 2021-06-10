package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class HomeDTO {
    public List<MenuDTO> menu;
    public List<FilterDTO> filters;
    public List<AuctionDTO> auctions;

}
