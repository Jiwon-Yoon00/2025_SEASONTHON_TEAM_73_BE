package com.season.livingmate.user.domain.repository;

import com.season.livingmate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    // isRoom 조건에 따른 사용자 조회
    List<User> findByIsRoomAndIdNot(Boolean isRoom, Long id);
}
