package com.uade.api.controllers.validators;

import com.uade.api.dtos.NewUserDTO;
import com.uade.api.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator {

    public void validateNewUser(NewUserDTO dto) {
        if (dto == null) {
            throw new BadRequestException();
        }

        nonEmptyString(dto.dni);
        nonEmptyString(dto.email);
        nonEmptyString(dto.phone);
        nonEmptyString(dto.address);
        nonEmptyString(dto.lastName);
        nonEmptyString(dto.firstName);
    }
}
