package org.jorion.simplesecurity.repository;

import org.jorion.simplesecurity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve the products from the DB. We only use methods inherited from the JpaRepository interface.
 */
public interface IProductRepository extends JpaRepository<Product, Integer>
{

}
