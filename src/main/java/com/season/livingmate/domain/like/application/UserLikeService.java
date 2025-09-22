package com.season.livingmate.domain.like.application;

import com.season.livingmate.domain.like.domain.entity.UserLike;
import com.season.livingmate.domain.user.domain.entity.User;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.user.api.dto.response.UserListRes;
import com.season.livingmate.domain.like.domain.repository.UserLikeRepository;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean createLike(Long targetUserId , CustomUserDetails userDetails){

        User user = userDetails.getUser();

        // 이미 좋아요를 눌렀는지 확인
        boolean exists = userLikeRepository.existsByUserIdAndLikedUserId(user.getId(), targetUserId);
        if (exists) {
            throw new CustomException(ErrorStatus.ALREADY_LIKED);
        }

        // 좋아요 대상 유저 조회
        User likedUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));


        UserLike userLike = UserLike.create(user, likedUser);
        userLikeRepository.save(userLike);

        return true;
    }

    // 좋아요 취소
    @Transactional
    public void deleteLike(Long targetUserId, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        UserLike like = userLikeRepository.findByUserIdAndLikedUserId(user.getId(), targetUserId)
                .orElseThrow(() -> new CustomException(ErrorStatus.LIKE_NOT_FOUND));

        userLikeRepository.delete(like);
    }


    // 좋아요 목록 조회
    @Transactional(readOnly = true)
    public Page<UserListRes> getLike(CustomUserDetails userDetails, Pageable pageable) {

        User user = userDetails.getUser();

        Page<UserLike> likedProfiles = userLikeRepository.findAllByUserId(user.getId(), pageable);

        return likedProfiles.map(UserListRes::from);
    }
}
