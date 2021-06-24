package com.uade.api.controllers;

import com.uade.api.controllers.validators.PaymentTypeValidator;
import com.uade.api.controllers.validators.UserValidator;
import com.uade.api.dtos.request.NewCodeDTO;
import com.uade.api.dtos.request.NewPasswordDTO;
import com.uade.api.dtos.request.NewPaymentMethodDTO;
import com.uade.api.dtos.request.NewUserDTO;
import com.uade.api.dtos.response.PaymentMethodsDTO;
import com.uade.api.dtos.response.UserDTO;
import com.uade.api.entities.Client;
import com.uade.api.marshallers.PaymentMethodMarshaller;
import com.uade.api.models.PaymentMethodModel;
import com.uade.api.models.PaymentMethodType;
import com.uade.api.models.UserModel;
import com.uade.api.services.ClientService;
import com.uade.api.services.PaymentMethodService;
import com.uade.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ClientService clientService;
    private final PaymentMethodService paymentMethodService;

    private final UserValidator userValidator;
    private final PaymentTypeValidator paymentTypeValidator;

    private final PaymentMethodMarshaller paymentMethodMarshaller;

    public UserController(UserService userService, ClientService clientService, PaymentMethodService paymentMethodService, UserValidator userValidator, PaymentTypeValidator paymentTypeValidator, PaymentMethodMarshaller paymentMethodMarshaller) {
        this.userService = userService;
        this.clientService = clientService;
        this.paymentMethodService = paymentMethodService;
        this.userValidator = userValidator;
        this.paymentTypeValidator = paymentTypeValidator;
        this.paymentMethodMarshaller = paymentMethodMarshaller;
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

        return UserDTO.of(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getClientStatus().value());
    }

    @GetMapping(value = "/login")
    public UserDTO login(@RequestParam String email, @RequestParam String password) {
        userValidator.validateEmailAndPassword(email, password);

        UserModel userModel = userService.getUser(email, password);

        return UserDTO.of(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getClientStatus().value());
    }

    @PostMapping(value = "/status")
    public ResponseEntity updateStatus(@RequestBody NewCodeDTO dto) {
        userValidator.validateNewCode(dto);

        UserModel model = userService.getUser(dto.email);
        clientService.updateUserStatus(model.getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/createCode")
    public ResponseEntity<String> generateCode(@RequestBody NewCodeDTO dto) {
        userValidator.validateNewCode(dto);

        String newCode = userService.updateUserCode(dto.email);

        return new ResponseEntity<>(newCode, HttpStatus.CREATED);
    }

    @PostMapping(value = "/code")
    public ResponseEntity generatePassword(@RequestBody NewPasswordDTO dto) {
        userValidator.validateNewPassword(dto);

        userService.updateUserPassword(dto.email, dto.code, dto.password);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}/payment_methods")
    public ResponseEntity addPaymentMethod(@PathVariable int id, @RequestBody NewPaymentMethodDTO dto) {
        paymentTypeValidator.validateNewPaymentMethod(dto);

        PaymentMethodModel paymentMethodModel = PaymentMethodModel.builder()
                .type(PaymentMethodType.fromString(dto.type))
                .bin(dto.bin)
                .company(dto.company)
                .number(dto.number)
                .build();

        Client client = clientService.getClientById(id);

        paymentMethodService.addPaymentMethod(paymentMethodModel, client);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/payment_methods")
    public PaymentMethodsDTO getPaymentMethods(@PathVariable int id) {
        List<PaymentMethodModel> paymentMethods = clientService.getClientPaymentMethods(id);

        return paymentMethodMarshaller.buildPaymentMethods(paymentMethods);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
