package com.uade.api.services;

import com.uade.api.entities.Client;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        return new UserModel(
                user.getId(),
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                UserStatus.fromString(user.getStatus()),
                user.getClient() != null ? CategoryType.fromString(user.getClient().getCategory()) : null,
                user.getClient() != null ? ClientStatus.fromString(user.getClient().getClientStatus()) : null
        );
    }

    public UserModel getUser(Integer userId) {
        UserModel model = null;
        if (userId != null) {
            User user = userRepository.findById(userId.intValue());

            if (user == null) {
                throw new NotFoundException();
            }

            model = new UserModel(
                    user.getId(),
                    user.getDni(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getPhone(),
                    UserStatus.fromString(user.getStatus()),
                    CategoryType.fromString(user.getClient().getCategory()),
                    ClientStatus.fromString(user.getClient().getClientStatus())
            );
        }

        return model;
    }

    public UserModel getUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException();
        }

        return new UserModel(
                user.getId(),
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                UserStatus.fromString(user.getStatus()),
                CategoryType.fromString(user.getClient().getCategory()),
                ClientStatus.fromString(user.getClient().getClientStatus())
        );
    }

    public String updateUserCode(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (ClientStatus.ADMITTED.value().equals(user.getClient().getClientStatus())) {
            throw new BadRequestException();
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
            throw new BadRequestException();
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
