package com.uade.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class UserModel {
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
