package com.season.livingmate.global.auth.security;

import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.user.domain.User;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    // 사용자의 이름(또는 ID, 이메일 등)을 입력받아 데이터베이스 등에서 사용자를 검색
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. " + username);
        }

        return new CustomUserDetails(user.get());
    }

    public UserDetails loadUserById(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorStatus.USER_NOT_FOUND);
        }
        return userRepository.findById(userId)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));
    }
}
