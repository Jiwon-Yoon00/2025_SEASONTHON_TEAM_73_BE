package com.season.livingmate.domain.userProfile.api.dto.response;


import com.season.livingmate.domain.like.domain.entity.UserLike;
import com.season.livingmate.domain.user.domain.entity.Gender;
import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.userProfile.domain.entity.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Schema(description = "유저프로필 응답 DTO")
@Getter
public class UserListRes {

    @Schema(description = "유저 ID", example = "1")
    private Long id;

    @Schema(description = "유저이미지", example = "url")
    private String userProfileImage;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "나이", example = "25")
    private int age;

    @Schema(description = "본인인증 완료 여부", example = "true")
    private boolean verified;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "증명서 제출 여부", example = "false")
    private WorkType isCertified;

    @Schema(description = "방 소유 여부", example = "true")
    private boolean room;

    @Schema(description = "흡연 여부", example = "false")
    private boolean smoking;

    @Schema(description = "부스터 사용 여부", example = "true")
    private boolean isBoosted;

    @Schema(description = "근무형태", example = "무직")
    private WorkType workType;

    public static UserListRes from(User user) {
        return new UserListRes(
                user.getId(),
                user.getUserProfile().getProfileImageUrl() != null ? user.getUserProfile().getProfileImageUrl() : null,
                user.getNickname(),
                user.getAge(),
                user.isVerified(),
                user.getGender(),
                user.getWorkType(),
                user.isHasRoom(),
                user.getUserProfile().isSmoking(),
                user.getUserBoost() != null,
                user.getUserProfile().getWorkType()
        );
    }

    public static UserListRes from(UserLike user) {
        User likedUser = user.getLikedUser();
        return new UserListRes(
                likedUser.getId(),
                likedUser.getUserProfile().getProfileImageUrl(),
                likedUser.getNickname(),
                likedUser.getAge(),
                likedUser.isVerified(),
                likedUser.getGender(),
                likedUser.getWorkType(),
                likedUser.isHasRoom(),
                likedUser.getUserProfile().isSmoking(),
                likedUser.getUserBoost() != null,
                likedUser.getUserProfile().getWorkType()
        );
    }
}
