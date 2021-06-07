package com.uade.api.repositories;

import com.uade.api.entities.Client;
import com.uade.api.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    Client findById(int id);
}
