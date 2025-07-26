package com.kshitij.mytutor.repository;

import com.kshitij.mytutor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Kshitij
 * @created 26-Jul-2025
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.* FROM users u WHERE u.username = ?1 OR u.email = ?2", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(String username, String email);

}
