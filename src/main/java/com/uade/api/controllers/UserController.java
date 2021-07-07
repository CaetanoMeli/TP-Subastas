package com.uade.api.controllers;

import com.uade.api.controllers.validators.PaymentTypeValidator;
import com.uade.api.controllers.validators.UserValidator;
import com.uade.api.dtos.request.NewArticleDTO;
import com.uade.api.dtos.request.NewBidDTO;
import com.uade.api.dtos.request.NewCodeDTO;
import com.uade.api.dtos.request.NewPasswordDTO;
import com.uade.api.dtos.request.NewPaymentMethodDTO;
import com.uade.api.dtos.request.NewUserDTO;
import com.uade.api.dtos.request.UpdateUserDTO;
import com.uade.api.dtos.response.PaymentMethodsDTO;
import com.uade.api.dtos.response.ProductDTO;
import com.uade.api.dtos.response.UserDTO;
import com.uade.api.entities.Client;
import com.uade.api.marshallers.ArticleMarshaller;
import com.uade.api.marshallers.PaymentMethodMarshaller;
import com.uade.api.models.PaymentMethodModel;
import com.uade.api.models.PaymentMethodType;
import com.uade.api.models.ProductModel;
import com.uade.api.models.UserModel;
import com.uade.api.services.BidService;
import com.uade.api.services.ClientService;
import com.uade.api.services.PaymentMethodService;
import com.uade.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final BidService bidService;
    private final PaymentMethodService paymentMethodService;

    private final UserValidator userValidator;
    private final PaymentTypeValidator paymentTypeValidator;

    private final PaymentMethodMarshaller paymentMethodMarshaller;
    private final ArticleMarshaller articleMarshaller;

    public UserController(UserService userService, ClientService clientService, BidService bidService, PaymentMethodService paymentMethodService, UserValidator userValidator, PaymentTypeValidator paymentTypeValidator, PaymentMethodMarshaller paymentMethodMarshaller, ArticleMarshaller articleMarshaller) {
        this.userService = userService;
        this.clientService = clientService;
        this.bidService = bidService;
        this.paymentMethodService = paymentMethodService;
        this.userValidator = userValidator;
        this.paymentTypeValidator = paymentTypeValidator;
        this.paymentMethodMarshaller = paymentMethodMarshaller;
        this.articleMarshaller = articleMarshaller;
    }

    @PostMapping(value = "")
    public ResponseEntity registerUser(@RequestBody NewUserDTO dto) {
        userValidator.validateNewUser(dto);

        UserModel model = UserModel.builder()
                .dni(dto.dni)
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .email(dto.email)
                .address(dto.address)
                .phone(dto.phone)
                .build();

        userService.registerUser(model);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping(value = "")
    public ResponseEntity updateUser(@RequestBody UpdateUserDTO dto) {
        userValidator.validateUpdateUser(dto);

        UserModel model = UserModel.builder()
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .password(dto.password)
                .address(dto.address)
                .phone(dto.phone)
                .email(dto.email)
                .build();

        userService.updateUser(model);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public UserModel getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/validate")
    public UserDTO validateEmail(@RequestParam String email) {
        userValidator.validateEmail(email);

        UserModel userModel = userService.getUser(email);

        return UserDTO.builder()
                .userId(userModel.getId())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .status(userModel.getClientStatus().value())
                .category(userModel.getCategory().value())
                .build();
    }

    @GetMapping(value = "/login")
    public UserDTO login(@RequestParam String email, @RequestParam String password) {
        userValidator.validateEmailAndPassword(email, password);

        UserModel userModel = userService.getUser(email, password);

        return UserDTO.builder()
                .userId(userModel.getId())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .status(userModel.getClientStatus().value())
                .category(userModel.getCategory().value())
                .build();
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

    @PutMapping(value = "/{id}/articles")
    public ResponseEntity addArticle(@PathVariable int id, @RequestBody NewArticleDTO dto) {
        userValidator.validateNewArticle(dto);

        ProductModel productModel = ProductModel.builder()
                .images(dto.images)
                .description(dto.description)
                .fullDescription(dto.fullDescription)
                .build();

        userService.addArticle(id, productModel);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}/articles/{articleId}")
    public ResponseEntity approveArticle(@PathVariable int id, @PathVariable int articleId) {
        userService.approveArticle(id, articleId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/articles")
    public List<ProductDTO> getArticles(@PathVariable int id) {
        return articleMarshaller.buildArticles(userService.getArticles(id));
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

    @DeleteMapping(value = "/{id}/payment_methods/{paymentMethodId}")
    public ResponseEntity deletePaymentMethod(@PathVariable int id, @PathVariable int paymentMethodId) {
        clientService.deletePaymentMethod(id, paymentMethodId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}/bids")
    public ResponseEntity addBid(@PathVariable int id, @RequestBody NewBidDTO dto) {
        userValidator.validateNewBid(dto);

        UserModel model = userService.getUser(id);

        bidService.addBid(model, dto.amount, dto.catalogId);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
