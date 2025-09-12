package com.season.livingmate.user.api.dto.response;

import com.season.livingmate.user.domain.UserProfileLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유저프로필 좋아요 목록 조회 응답 DTO")
public class LikeGetResDto {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "로그인한 유저 ID", example = "1")
    private Long userId;

    @Schema(description = "좋아요 받은 유저 ID", example = "2")
    private Long likedUserId;

    @Schema(description = "좋아요 받은 유저 닉네임", example = "하마")
    private String nickname;

//    @Schema(description = "좋아요 받은 유저 정보", example = "형식미정..")
//    private  String info;

    @Schema(description = "좋아요 받은 유저 프로필 이미지", example = "url")
    private String profileImageUrl;

    public static LikeGetResDto from(UserProfileLike userProfileLike) {
        return new LikeGetResDto(
                userProfileLike.getId(),
                userProfileLike.getUser().getId(),
                userProfileLike.getLikedUser().getId(),
                userProfileLike.getLikedUser().getNickname(),
                // userProfileLike.getLikedUserId().getBio(),
                userProfileLike.getLikedUser().getUserProfile().getProfileImageUrl()
        );
    }
}
