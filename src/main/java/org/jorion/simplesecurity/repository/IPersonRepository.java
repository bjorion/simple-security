package org.jorion.simplesecurity.repository;

import org.jorion.simplesecurity.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IPersonRepository extends JpaRepository<Person, Integer> {

    /**
     * @return an Optional instance containing the Person entity.
     */
    Optional<Person> findPersonByUsername(@Param(value = "username") String username);
}
