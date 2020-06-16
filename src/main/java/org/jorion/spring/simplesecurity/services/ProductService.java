package org.jorion.spring.simplesecurity.services;

import java.util.List;

import org.jorion.spring.simplesecurity.entities.Product;
import org.jorion.spring.simplesecurity.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service uses the {@link ProductService} to retrieve all the products from the db.
 */
@Service
public class ProductService
{
	@Autowired
	private IProductRepository productRepository;

	// --- Methods ---
	public List<Product> findAll()
	{
		List<Product> products = productRepository.findAll();
		return products;
	}
}
