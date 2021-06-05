package com.uade.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class UserModel {
    private final String dni;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String address;
    private final String phone;
    private UserStatus status;
}
