package com.uade.api.repositories;

import com.uade.api.entities.Auction;
import com.uade.api.entities.Catalog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CatalogRepository extends CrudRepository<Catalog, Integer> {
    List<Catalog> findAll();
}
