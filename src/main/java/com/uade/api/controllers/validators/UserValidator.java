package com.uade.api.controllers.validators;

import com.uade.api.dtos.request.NewArticleDTO;
import com.uade.api.dtos.request.NewBidDTO;
import com.uade.api.dtos.request.NewCodeDTO;
import com.uade.api.dtos.request.NewPasswordDTO;
import com.uade.api.dtos.request.NewUserDTO;
import com.uade.api.dtos.request.UpdateUserDTO;
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

    public void validateUpdateUser(UpdateUserDTO dto) {
        nonNull(dto);
    }

    public void validateNewCode(NewCodeDTO dto) {
        nonNull(dto);
        validateEmail(dto.email);
    }

    public void validateNewPassword(NewPasswordDTO dto) {
        nonNull(dto);
        nonNull(dto.code);
        validateEmailAndPassword(dto.email, dto.password);
    }

    public void validateNewArticle(NewArticleDTO dto) {
        nonNull(dto.images);
        dto.images.forEach(this::nonEmptyString);
        nonEmptyString(dto.description);
        nonEmptyString(dto.fullDescription);
    }

    public void validateEmailAndPassword(String email, String password) {
        validateEmail(email);
        validatePassword(password);
    }

    public void validateEmail(String email) {
        nonEmptyString(email);
    }

    public void validateNewBid(NewBidDTO dto) {
        nonNull(dto);
        nonNull(dto.catalogId);
        nonNull(dto.amount);
    }

    private void validatePassword(String password) {
        nonEmptyString(password);
    }
}
