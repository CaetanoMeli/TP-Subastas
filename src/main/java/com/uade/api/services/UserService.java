package com.uade.api.services;

import com.uade.api.entities.User;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.exceptions.InternalServerException;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.UserModel;
import com.uade.api.models.UserStatus;
import com.uade.api.repositories.UserRepository;
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

    public UserModel getUser(int userId) {
        User user = userRepository.findById(userId);
        UserModel model = null;

        if (user != null) {
            model = new UserModel(
                    user.getDni(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getPhone(),
                    UserStatus.fromString(user.getStatus())
            );
        }

        return model;
    }

    public UserModel getUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        return new UserModel(
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                UserStatus.fromString(user.getStatus())
        );
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
                user.getDni(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                UserStatus.fromString(user.getStatus())
        );
    }

    public void updateUserPassword(String email, Integer code, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (user.getCode() == null || !user.getCode().equals(code)) {
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
