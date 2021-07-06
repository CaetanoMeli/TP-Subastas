package com.uade.api.services;

import com.uade.api.entities.Employee;
import com.uade.api.entities.Owner;
import com.uade.api.entities.Picture;
import com.uade.api.entities.Product;
import com.uade.api.exceptions.NotFoundException;
import com.uade.api.models.ProductModel;
import com.uade.api.repositories.EmployeeRepository;
import com.uade.api.repositories.PictureRepository;
import com.uade.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PictureRepository pictureRepository;
    private final EmployeeRepository employeeRepository;

    public ProductService(ProductRepository productRepository, PictureRepository pictureRepository, EmployeeRepository employeeRepository) {
        this.productRepository = productRepository;
        this.pictureRepository = pictureRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void addProduct(ProductModel model, Owner owner) {
        Employee employee = assignEmployee();

        Product product = new Product();
        product.setOwner(owner);
        product.setEmployee(employee);
        product.setAvailable("no");
        product.setCatalogDescription(model.getFullDescription());
        product.setCompleteDescription(model.getDescription());
        product.setDate(new Date());

        productRepository.save(product);

        model.getImages().forEach(image -> {
            Picture picture = new Picture();
            picture.setPhoto(image);
            picture.setProduct(product);

            pictureRepository.save(picture);
        });
    }

    private Employee assignEmployee() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
