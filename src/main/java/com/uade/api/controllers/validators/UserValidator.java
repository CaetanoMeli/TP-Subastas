package com.uade.api.controllers.validators;

import com.uade.api.dtos.request.NewPasswordDTO;
import com.uade.api.dtos.request.NewUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator {

    public void validateNewUser(NewUserDTO dto) {
        nonNull(dto);
        nonEmptyString(dto.dni);
        validateEmail(dto.email);
        nonEmptyString(dto.phone);
        nonEmptyString(dto.address);
        nonEmptyString(dto.lastName);
        nonEmptyString(dto.firstName);
    }

    public void validateNewPassword(NewPasswordDTO dto) {
        nonNull(dto);
        nonNull(dto.code);
        validateEmailAndPassword(dto.email, dto.password);
    }

    public void validateEmailAndPassword(String email, String password) {
        validateEmail(email);
        validatePassword(password);
    }

    public void validateEmail(String email) {
        nonEmptyString(email);
    }

    private void validatePassword(String password) {
        nonEmptyString(password);
    }
}
