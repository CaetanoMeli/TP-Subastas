package com.uade.api.repositories;

import com.uade.api.entities.Auction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuctionRepository extends CrudRepository<Auction, Integer> {
    List<Auction> findAll();
}
