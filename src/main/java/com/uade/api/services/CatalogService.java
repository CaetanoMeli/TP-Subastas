package com.uade.api.services;

import com.uade.api.entities.Catalog;
import com.uade.api.models.CatalogModel;
import com.uade.api.repositories.CatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<CatalogModel> getCatalogs() {
        List<Catalog> catalogs = catalogRepository.findAll();
        List<CatalogModel> model = null;

        if (catalogs != null && catalogs.size() > 0) {
            model = catalogs.stream()
                    .map(catalog -> CatalogModel.of(
                            catalog.getId(),
                            catalog.getAuction(),
                            catalog.getOwner(),
                            catalog.getDescription()
                            )
                    )
                    .collect(Collectors.toList());
        }

        return model;
    }
}
