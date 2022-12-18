package org.jorion.simplesecurity.service;

import java.util.List;

import org.jorion.simplesecurity.entity.Product;
import org.jorion.simplesecurity.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service uses the {@link ProductService} to retrieve all the products from the db.
 */
@Service
@Transactional(readOnly = true)
public class ProductService
{
    @Autowired
    private IProductRepository productRepository;

    public List<Product> findAll()
    {
        List<Product> products = productRepository.findAll();
        return products;
    }
}
