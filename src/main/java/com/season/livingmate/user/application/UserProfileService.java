package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.api.dto.response.UserProfileResDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileCreateReqDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileUpdateReqDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserProfileRepository;
import com.season.livingmate.user.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserProfileResDto create(UserProfileCreateReqDto userProfileCreateReqDto, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));


        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.PROFILE_NOT_FOUND));
        userProfile.updateFromCreateDto(userProfileCreateReqDto);
        return  UserProfileResDto.from(userProfile);
    }

    @Transactional
    public UserProfileResDto update (UserProfileUpdateReqDto userProfileUpdateReqDto, CustomUserDetails userDetails){

        System.out.println("유저프로필 수정" + userDetails.getUser().getId());

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        userProfile.update(userProfileUpdateReqDto);

        return UserProfileResDto.from(userProfile);
    }

    // 조회 - 내 프로필
    @Transactional(readOnly = true)
    public UserProfileResDto getMyProfile(CustomUserDetails userDetails) {
        UserProfile profile = userProfileRepository.findByUserId(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserProfileResDto.from(profile);
    }

    // 상대방 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileResDto getOtherProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return UserProfileResDto.from(profile);
    }
}
