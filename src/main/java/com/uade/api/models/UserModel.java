package com.uade.api.models;

import com.uade.api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserModel {
    private final Integer id;
    private final String dni;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String address;
    private final String phone;
    private final String password;
    private final User entity;
    private final boolean hasActiveBid;
    private UserStatus status;
    private CategoryType category;
    private ClientStatus clientStatus;
}
