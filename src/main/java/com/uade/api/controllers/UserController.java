package com.uade.api.controllers;

import com.uade.api.controllers.validators.UserValidator;
import com.uade.api.dtos.request.NewPasswordDTO;
import com.uade.api.dtos.request.NewUserDTO;
import com.uade.api.dtos.response.UserDTO;
import com.uade.api.models.UserModel;
import com.uade.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping(value = "")
    public ResponseEntity registerUser(@RequestBody NewUserDTO dto) {
        userValidator.validateNewUser(dto);

        UserModel model = UserModel.of(null, dto.dni, dto.firstName, dto.lastName, dto.email, dto.address, dto.phone);

        userService.registerUser(model);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/validate")
    public UserDTO validateEmail(@RequestParam String email) {
        userValidator.validateEmail(email);

        UserModel userModel = userService.getUser(email);

        return UserDTO.of(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getStatus().value());
    }

    @GetMapping(value = "/login")
    public UserDTO login(@RequestParam String email, @RequestParam String password) {
        userValidator.validateEmailAndPassword(email, password);

        UserModel userModel = userService.getUser(email, password);

        return UserDTO.of(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getStatus().value());
    }

    @PostMapping(value = "/code")
    public ResponseEntity generatePassword(@RequestBody NewPasswordDTO dto) {
        userValidator.validateNewPassword(dto);

        userService.updateUserPassword(dto.email, dto.code, dto.password);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
