package org.jorion.simplesecurity.repository;

import org.jorion.simplesecurity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve the products from the DB.
 */
public interface IProductRepository extends JpaRepository<Product, Integer> {

}
