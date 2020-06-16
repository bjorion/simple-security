package org.jorion.spring.simplesecurity.repositories;

import java.util.Optional;

import org.jorion.spring.simplesecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve the users by their usernames from the database. The methods declared in the interface will be
 * implemented by SpringData, and it will execute a query based on its name.
 */
public interface IUserRepository extends JpaRepository<User, Integer>
{
	/**
	 * @return an Optional instance containing the User entity. If no such user exists, returns an empty Optional.
	 */
	Optional<User> findUserByUsername(String username);
}
