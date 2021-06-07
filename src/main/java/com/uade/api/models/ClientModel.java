package com.uade.api.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class ClientModel {
    private final CategoryType categoryType;
    private final ClientStatus clientStatus;
}
