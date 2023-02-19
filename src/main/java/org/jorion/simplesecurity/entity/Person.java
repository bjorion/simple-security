package org.jorion.simplesecurity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jorion.simplesecurity.entity.enums.EncryptionAlgorithm;

import java.util.List;

/**
 * Corresponds to the PERSON table.
 */
@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm algorithm;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Authority> authorities;

}
