package com.uade.api.services;

import com.uade.api.entities.User;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.exceptions.InternalServerException;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.CategoryType;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.UserModel;
import com.uade.api.models.UserStatus;
import com.uade.api.repositories.UserRepository;
import com.uade.api.utils.RandomNumberGenerator;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CatalogService catalogService;

    public UserService(UserRepository userRepository, CatalogService catalogService) {
        this.userRepository = userRepository;
        this.catalogService = catalogService;
    }

    public void registerUser(UserModel userModel) {
        User user = mapModelToEntity(userModel);
        user.setStatus(UserStatus.INACTIVE.value());

        try {
            userRepository.save(user);
        } catch(Exception e) {
            throw new InternalServerException();
        }
    }

    public UserModel getUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        return UserModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .status(UserStatus.fromString(user.getStatus()))
                .category(user.getClient() != null ? CategoryType.fromString(user.getClient().getCategory()) : null)
                .clientStatus(user.getClient() != null ? ClientStatus.fromString(user.getClient().getClientStatus()) : null)
                .build();
    }

    public UserModel getUser(Integer userId) {
        return getUser(userId, false);
    }

    public UserModel getUser(Integer userId, boolean addBidInfo) {
        UserModel model = null;
        if (userId != null) {
            User user = userRepository.findById(userId.intValue());
            boolean userHasActiveBid = false;

            if (user == null) {
                throw new NotFoundException();
            }

            if (addBidInfo) {
                userHasActiveBid = user.getClient().getBids()
                        .stream()
                        .anyMatch(bid -> !catalogService.isAuctioned(bid.getCatalog()));
            }

            model = UserModel.builder()
                    .id(user.getId())
                    .dni(user.getDni())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .entity(user)
                    .hasActiveBid(userHasActiveBid)
                    .status(UserStatus.fromString(user.getStatus()))
                    .category(CategoryType.fromString(user.getClient().getCategory()))
                    .clientStatus(ClientStatus.fromString(user.getClient().getClientStatus()))
                    .build();
        }

        return model;
    }

    public UserModel getUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("invalid_password");
        }

        return UserModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .status(UserStatus.fromString(user.getStatus()))
                .category(CategoryType.fromString(user.getClient().getCategory()))
                .clientStatus(ClientStatus.fromString(user.getClient().getClientStatus()))
                .build();
    }

    public String updateUserCode(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (ClientStatus.ADMITTED.value().equals(user.getClient().getClientStatus())) {
            throw new BadRequestException("non_admitted_user");
        }

        String newCode = RandomNumberGenerator.generateRandomNumber();

        user.setCode(newCode);

        userRepository.save(user);

        return newCode;
    }

    public void updateUserPassword(String email, String code, String password) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (ClientStatus.ADMITTED.value().equals(user.getClient().getClientStatus()) && user.getCode() == null || !user.getCode().equals(code)) {
            throw new BadRequestException("non_admitted_user_or_invalid_code");
        }

        user.setPassword(password);

        userRepository.save(user);
    }

    private User mapModelToEntity(UserModel userModel) {
        User user = new User();

        user.setDni(userModel.getDni());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setAddress(userModel.getAddress());

        return user;
    }
}
