package com.uade.api.dtos.response;

import lombok.Builder;

@Builder
public class UserDTO {
    public Integer userId;
    public String firstName;
    public String lastName;
    public String status;
    public String category;
}
