package org.jorion.simplesecurity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jorion.simplesecurity.entity.enums.Currency;

/**
 * Corresponds to the PRODUCT table.
 */
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
