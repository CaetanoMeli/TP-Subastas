package com.uade.api.dtos.response;

import lombok.Builder;

@Builder
public class UserDTO {
    public Integer userId;
    public String dni;
    public String email;
    public String phone;
    public String address;
    public String firstName;
    public String lastName;
    public String status;
    public String category;
}
