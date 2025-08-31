package com.season.livingmate.auth.security;

import com.season.livingmate.user.entity.User;
import com.season.livingmate.user.repository.UserRepository;
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
}
