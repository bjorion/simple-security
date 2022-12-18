package org.jorion.simplesecurity.entity;

import java.util.List;

import org.jorion.simplesecurity.entity.enums.EncryptionAlgorithm;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Corresponds to the USER table.
 */
@Entity
@Table(name = "person")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm algorithm;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Authority> authorities;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public EncryptionAlgorithm getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(EncryptionAlgorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    public List<Authority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities)
    {
        this.authorities = authorities;
    }
}
