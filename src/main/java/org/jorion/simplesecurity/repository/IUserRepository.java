package org.jorion.simplesecurity.repository;

import java.util.Optional;

import org.jorion.simplesecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve the users by their usernames from the database. The methods declared in the interface will be
 * implemented by SpringData, and it will execute a query based on its name.
 */
public interface IUserRepository extends JpaRepository<User, Integer>
{
    /**
     * @return an Optional instance containing the User entity. If no such user exists, returns an empty Optional.
     */
    Optional<User> findUserByUsername(@Param(value = "username") String username);
}
