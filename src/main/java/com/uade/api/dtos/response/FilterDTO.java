package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class FilterDTO {
    public String name;
    public List<FilterType> types;

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    public static class FilterType {
        String name;
    }
}


