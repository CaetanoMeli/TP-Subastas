package com.uade.api.services;

import com.uade.api.entities.User;
import com.uade.api.exceptions.InternalServerException;
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

    private User mapModelToEntity(UserModel userModel) {
        User user = new User();

        user.setDni(userModel.getDni());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setAddress(userModel.getAddress());

        return user;
    }

}
