package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.api.dto.response.UserListResDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserListResDto getUser(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserListResDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserListResDto getOther(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserListResDto.from(user);
    }
}
