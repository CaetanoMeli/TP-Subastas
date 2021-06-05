package com.uade.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class UserDTO {
    public String firstName;
    public String lastName;
    public String status;
}
