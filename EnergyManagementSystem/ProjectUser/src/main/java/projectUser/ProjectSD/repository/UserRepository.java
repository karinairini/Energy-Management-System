package projectUser.ProjectSD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projectUser.ProjectSD.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing UserEntity objects in the database.
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Finds a list of UserEntity objects with CLIENT role.
     *
     * @return a list of UserEntity objects with CLIENT role
     */
    @Query(value = "SELECT user " +
            "FROM UserEntity user " +
            "WHERE user.role = 'CLIENT' ")
    List<UserEntity> findAllClients();

    /**
     * Finds an optional UserEntity object by their name and age greater than or equal to 60.
     *
     * @param name the name of the user to find
     * @return an Optional containing the UserEntity object if found, or empty if not found
     */
    @Query(value = "SELECT user " +
            "FROM UserEntity user " +
            "WHERE user.name = :name " +
            "AND user.age >= 60  ")
    Optional<UserEntity> findSeniorUsersByName(@Param("name") String name);

    /**
     * Finds an optional UserEntity object by their email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the UserEntity object if found, or empty if not found
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a list of UserEntity objects with ADMIN role.
     *
     * @return a list of UserEntity objects with ADMIN role
     */
    @Query(value = "SELECT user " +
            "FROM UserEntity user " +
            "WHERE user.role = 'ADMIN' ")
    List<UserEntity> findAllAdmins();
}
