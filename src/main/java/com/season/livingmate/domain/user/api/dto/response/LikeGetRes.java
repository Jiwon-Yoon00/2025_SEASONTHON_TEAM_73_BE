package com.season.livingmate.domain.user.api.dto.response;

import com.season.livingmate.domain.user.domain.Gender;
import com.season.livingmate.domain.user.domain.UserProfileLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유저프로필 좋아요 목록 조회 응답 DTO")
public class LikeGetRes {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "로그인한 유저 ID", example = "1")
    private Long userId;

    @Schema(description = "좋아요 받은 유저 ID", example = "2")
    private Long likedUserId;

    @Schema(description = "좋아요 받은 유저 닉네임", example = "하마")
    private String nickname;

    @Schema(description = "성별", example = "여자")
    private Gender gender;

    @Schema(description = "나이", example = "25")
    private int age;

    @Schema(description = "흡연유무", example = "false")
    private boolean isSmoking;


    @Schema(description = "좋아요 받은 유저 프로필 이미지", example = "url")
    private String profileImageUrl;

    public static LikeGetRes from(UserProfileLike userProfileLike) {
        return new LikeGetRes(
                userProfileLike.getId(),
                userProfileLike.getUser().getId(),
                userProfileLike.getLikedUser().getId(),
                userProfileLike.getLikedUser().getNickname(),
                // userProfileLike.getLikedUserId().getBio(),
                userProfileLike.getLikedUser().getGender(),
                userProfileLike.getLikedUser().getAge(),
                userProfileLike.getLikedUser().getUserProfile().isSmoking(),
                userProfileLike.getLikedUser().getUserProfile().getProfileImageUrl()
        );
    }
}
