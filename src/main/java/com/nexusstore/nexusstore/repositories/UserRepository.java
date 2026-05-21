package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for {@link UserModel} persistence.
 */
@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    /**
     * Finds a user by their exact username.
     *
     * @param username the login username to look up
     * @return an {@link Optional} containing the user if found
     */
    Optional<UserModel> findByUsername(String username);

    /**
     * Checks whether a username is already registered.
     *
     * @param username the username to check
     * @return {@code true} if the username is already taken
     */
    boolean existsByUsername(String username);
}