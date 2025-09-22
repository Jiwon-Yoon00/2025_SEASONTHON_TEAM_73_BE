package com.season.livingmate.domain.user.application;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.domain.user.domain.repository.UserBoostRepository;
import com.season.livingmate.domain.user.domain.repository.UserProfileRepository;
import com.season.livingmate.domain.user.domain.repository.UserProfileRepositoryImpl;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.post.domain.repository.PostRepository;
import com.season.livingmate.domain.user.api.dto.response.UserProfileRes;
import com.season.livingmate.domain.user.api.dto.response.UserListRes;
import com.season.livingmate.domain.user.api.dto.resquest.UserFilterReq;
import com.season.livingmate.domain.user.api.dto.resquest.UserProfileCreateReq;
import com.season.livingmate.domain.user.api.dto.resquest.UserProfileUpdateReq;
import com.season.livingmate.domain.user.domain.User;
import com.season.livingmate.domain.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileRepositoryImpl userProfileRepositoryImpl;
    private final PostRepository postRepository;
    private final UserBoostRepository userBoostRepository;

    @Transactional
    public UserProfileRes create(UserProfileCreateReq userProfileCreateReq, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));


        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.PROFILE_NOT_FOUND));
        userProfile.updateFromCreateDto(userProfileCreateReq);

        user.setPersonalitySurveyCompleted(true);
        userRepository.save(user); // 변경된 USER 엔티티 저장

        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());
        return  UserProfileRes.from(userProfile, myPosts);
    }

    @Transactional
    public UserProfileRes update (UserProfileUpdateReq userProfileUpdateReq, CustomUserDetails userDetails){

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        userProfile.update(userProfileUpdateReq, user);
        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());
        return UserProfileRes.from(userProfile, myPosts);
    }

    // 조회 - 내 프로필
    @Transactional(readOnly = true)
    public UserProfileRes getMyProfile(CustomUserDetails userDetails) {
        UserProfile profile = userProfileRepository.findByUserId(userDetails.getUserId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        List<Post> myPosts = postRepository.findAllByUserId(userDetails.getUserId());

        return UserProfileRes.from(profile, myPosts);
    }

    // 상대방 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileRes getOtherProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));
        List<Post> getPosts = postRepository.findAllByUserId(userId);
        return UserProfileRes.from(profile, getPosts);
    }

    // 모든 유저의 프로필 정보를 조회하는 메서드
    @Transactional(readOnly = true)
    public Page<UserListRes> getAllUserProfiles(CustomUserDetails userDetails, Pageable pageable) {
        Long loggedInUserId = userDetails.getUserId();

        // 부스트 유저 아이디 리스트
        List<Long> boostUserIds = userBoostRepository.findBoostUserIds();
        if (boostUserIds.isEmpty()) {
            boostUserIds = Collections.singletonList(-1L);
        }

        Page<UserProfile> profiles = userProfileRepositoryImpl.filterWithBoostFirst(
                null,
                userDetails.getUser().getGender(),
                loggedInUserId,
                boostUserIds,
                pageable
        );

        return profiles.map(profile -> UserListRes.from(profile.getUser()));
    }

    // 필터링된 유저 프로필 조회
    @Transactional(readOnly = true)
    public Page<UserListRes> filterUsers(UserFilterReq dto, CustomUserDetails userDetails, Pageable pageable) {
        Long loggedInUserId = userDetails.getUserId();

        List<Long> boostUserIds = userBoostRepository.findBoostUserIds();
        if (boostUserIds.isEmpty()) {
            boostUserIds = Collections.singletonList(-1L);
        }

        Page<UserProfile> profiles = userProfileRepositoryImpl.filterWithBoostFirst(
                dto,
                userDetails.getUser().getGender(),
                loggedInUserId,
                boostUserIds,
                pageable
        );

        return profiles.map(profile -> UserListRes.from(profile.getUser()));
    }
}
