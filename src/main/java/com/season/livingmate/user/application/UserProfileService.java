package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.repository.PostRepository;
import com.season.livingmate.user.api.dto.response.UserProfileResDto;
import com.season.livingmate.user.api.dto.response.UserResDto;
import com.season.livingmate.user.api.dto.resquest.UserFilterReqDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileCreateReqDto;
import com.season.livingmate.user.api.dto.resquest.UserProfileUpdateReqDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserProfileRepository;
import com.season.livingmate.user.domain.repository.UserRepository;
import com.season.livingmate.user.domain.repository.UserSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public UserProfileResDto create(UserProfileCreateReqDto userProfileCreateReqDto, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));


        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.PROFILE_NOT_FOUND));
        userProfile.updateFromCreateDto(userProfileCreateReqDto);

        user.setPersonalitySurveyCompleted(true);
        userRepository.save(user); // 변경된 USER 엔티티 저장

        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());
        return  UserProfileResDto.from(userProfile, myPosts);
    }

    @Transactional
    public UserProfileResDto update (UserProfileUpdateReqDto userProfileUpdateReqDto, CustomUserDetails userDetails){

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        userProfile.update(userProfileUpdateReqDto);
        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());
        return UserProfileResDto.from(userProfile, myPosts);
    }

    // 조회 - 내 프로필
    @Transactional(readOnly = true)
    public UserProfileResDto getMyProfile(CustomUserDetails userDetails) {
        UserProfile profile = userProfileRepository.findByUserId(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());

        return UserProfileResDto.from(profile, myPosts);
    }

    // 상대방 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileResDto getOtherProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));
        List<Post> getPosts = postRepository.findAllByUserId(userId);
        return UserProfileResDto.from(profile, getPosts);
    }

    // 모든 유저의 프로필 정보를 조회하는 메서드
    @Transactional(readOnly = true)
    public Page<UserResDto> getAllUserProfiles(CustomUserDetails userDetails, Pageable pageable) {
        Long loggedInUserId = userDetails.getUserId();

        // 같은 성별, 본인 제외
        Specification<UserProfile> spec = UserSpec.matchUserGender(userDetails.getUser().getGender())
                .and(UserSpec.excludeUser(loggedInUserId));

        Page<UserProfile> userProfiles = userProfileRepository.findAll(spec, pageable);
        return userProfiles.map(profiles -> UserResDto.from(profiles.getUser()));
    }

    // 필터링된 유저 프로필 조회
    @Transactional(readOnly = true)
    public Page<UserResDto> filterUsers(UserFilterReqDto dto, CustomUserDetails userDetails, Pageable pageable) {
        Long loggedInUserId = userDetails.getUserId();

        Specification<UserProfile> spec = UserSpec.build(dto, userDetails.getUser().getGender(), loggedInUserId);
        Page<UserProfile> profiles = userProfileRepository.findAll(spec, pageable);
        return profiles.map(profile -> UserResDto.from(profile.getUser()));
    }
}
