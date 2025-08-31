package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.api.dto.response.ResDto;
import com.season.livingmate.user.api.dto.resquest.CreateReqDto;
import com.season.livingmate.user.api.dto.resquest.UpdateReqDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserProfileRepository;
import com.season.livingmate.user.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResDto create(CreateReqDto createReqDto, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));


        // 이미 프로필이 있으면 예외 처리
        if (userProfileRepository.existsByUser(user)) {
            throw new CustomException(ErrorStatus.DUPLICATE_RESOURCE);
        }

        UserProfile userProfile = createReqDto.toEntity(user);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        return ResDto.from(savedProfile);
    }

    @Transactional
    public ResDto update (UpdateReqDto updateReqDto, CustomUserDetails userDetails){

        System.out.println("유저프로필 수정" + userDetails.getUser().getId());

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        userProfile.update(updateReqDto);

        return ResDto.from(userProfile);
    }

    @Transactional(readOnly = true)
    public ResDto getMyProfile(CustomUserDetails userDetails) {
        UserProfile profile = userProfileRepository.findByUserId(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return ResDto.from(profile);
    }

    // 상대방 프로필 조회
    @Transactional(readOnly = true)
    public ResDto getOtherProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return ResDto.from(profile);
    }
}
