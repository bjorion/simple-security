package org.jorion.simplesecurity.service;

import org.jorion.simplesecurity.entity.Product;
import org.jorion.simplesecurity.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service uses the {@link ProductService} to retrieve all the products from the db.
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private IProductRepository productRepository;

    public List<Product> findAll() {

        return productRepository.findAll();
    }
}
