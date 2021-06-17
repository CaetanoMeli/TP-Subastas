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
import com.uade.api.repositories.ClientRepository;
import com.uade.api.repositories.UserRepository;
import com.uade.api.utils.RandomNumberGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void updateUserStatus(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        optionalClient.ifPresentOrElse(client -> {
            client.setClientStatus(ClientStatus.ADMITTED.value());
            clientRepository.save(client);
        }, () -> {
            throw new NotFoundException();
        });
    }
}
