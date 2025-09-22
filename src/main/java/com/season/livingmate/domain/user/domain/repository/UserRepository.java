package com.season.livingmate.domain.user.domain.repository;


import com.season.livingmate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username); // 아이디
    Optional<User> findByUsername(String username);

    // isRoom 조건에 따른 사용자 조회
    List<User> findByIsRoomAndIdNot(Boolean isRoom, Long id);

    boolean  existsByNickname(String nickname);
}
