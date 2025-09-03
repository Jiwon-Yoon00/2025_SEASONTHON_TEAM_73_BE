package com.season.livingmate.user.domain.repository;

import com.season.livingmate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

}
