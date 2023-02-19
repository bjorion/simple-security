package org.jorion.simplesecurity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Corresponds to the AUTHORITY table.
 */
@Getter
@Setter
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "person_fk")
    private Person person;
}
