package com.uade.api.controllers;

import com.uade.api.controllers.validators.UserValidator;
import com.uade.api.dtos.NewUserDTO;
import com.uade.api.models.UserModel;
import com.uade.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

        UserModel model = UserModel.of(dto.dni, dto.firstName, dto.lastName, dto.email, dto.address, dto.phone);

        userService.registerUser(model);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/validate")
    public ResponseEntity validateEmail() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/login")
    public ResponseEntity login() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
