package com.voting.userauth.repository;

import com.voting.userauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author michaelmak
 */

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
