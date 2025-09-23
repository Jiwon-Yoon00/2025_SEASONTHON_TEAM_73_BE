package com.season.livingmate.domain.user.application;

import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.userProfile.api.dto.response.UserListRes;

import com.season.livingmate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserListRes getUser(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserListRes.from(user);
    }

    @Transactional(readOnly = true)
    public UserListRes getOther(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserListRes.from(user);
    }
}
