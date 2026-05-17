package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JDBC repository for {@link UserModel} persistence.
 *
 * <p>Extends {@link CrudRepository} to inherit standard CRUD operations.
 * Spring Boot auto-generates the implementation at runtime via IoC/DI.
 *
 * <p>Custom query methods use Spring Data's derived query naming convention.
 */
@Repository
public interface UserRepository extends CrudRepository<UserModel, Integer> {

    /**
     * Finds a user by their username (case-insensitive via LIKE would need @Query;
     * this exact-match version uses Spring Data naming convention).
     * SQL derived: {@code SELECT * FROM users WHERE username = ?}
     *
     * @param username the login username to look up
     * @return an {@link Optional} containing the user if found
     */
    Optional<UserModel> findByUsername(String username);

    /**
     * Checks whether a username is already registered.
     * SQL derived: {@code SELECT COUNT(*) > 0 FROM users WHERE username = ?}
     *
     * @param username the username to check
     * @return {@code true} if the username is already taken
     */
    boolean existsByUsername(String username);
}
