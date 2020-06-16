package org.jorion.spring.simplesecurity.repositories;

import org.jorion.spring.simplesecurity.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve the products from the DB. We only use methods inherited from the JpaRepository interface.
 */
public interface IProductRepository extends JpaRepository<Product, Integer>
{

}
