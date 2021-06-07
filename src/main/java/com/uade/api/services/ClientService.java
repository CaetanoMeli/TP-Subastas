package com.uade.api.services;

import com.uade.api.entities.Client;
import com.uade.api.entities.User;
import com.uade.api.exceptions.BadRequestException;
import com.uade.api.exceptions.InternalServerException;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.CategoryType;
import com.uade.api.models.ClientModel;
import com.uade.api.models.ClientStatus;
import com.uade.api.models.UserModel;
import com.uade.api.models.UserStatus;
import com.uade.api.repositories.ClientRepository;
import com.uade.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientModel getClient(int userId) {
        Client client = clientRepository.findById(userId);
        ClientModel model = null;

        if (client != null) {
            model = ClientModel.of(
                CategoryType.fromString(client.getCategory()),
                ClientStatus.fromString(client.getClientStatus())
            );
        }

        return model;
    }

}
