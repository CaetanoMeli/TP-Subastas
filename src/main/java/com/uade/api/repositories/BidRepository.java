package com.uade.api.repositories;

import com.uade.api.entities.Bid;
import com.uade.api.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Integer> {}
