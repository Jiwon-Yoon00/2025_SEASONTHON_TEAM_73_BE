package com.season.livingmate.user.api.dto.response;

import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfileLike;
import com.season.livingmate.user.domain.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Schema(description = "유저프로필 응답 DTO")
@Getter
public class UserListResDto {

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
    private boolean isCertified;

    @Schema(description = "방 소유 여부", example = "true")
    private boolean room;

    @Schema(description = "흡연 여부", example = "false")
    private boolean smoking;

    @Schema(description = "부스터 사용 여부", example = "true")
    private boolean isBoosted;

    @Schema(description = "근무형태", example = "무직")
    private WorkType workType;

    public static UserListResDto from(User user) {
        return new UserListResDto(
                user.getId(),
                user.getUserProfile().getProfileImageUrl(),
                user.getNickname(),
                user.getAge(),
                user.isVerified(),
                user.getGender(),
                user.isCertified(),
                user.isRoom(),
                user.getUserProfile().isSmoking(),
                user.getUserBoost() != null,
                user.getUserProfile().getWorkType()
        );
    }

    public static UserListResDto from(UserProfileLike user) {
        return new UserListResDto(
                user.getId(),
                user.getUser().getUserProfile().getProfileImageUrl(),
                user.getUser().getNickname(),
                user.getUser().getAge(),
                user.getUser().isVerified(),
                user.getUser().getGender(),
                user.getUser().isCertified(),
                user.getUser().isRoom(),
                user.getUser().getUserProfile().isSmoking(),
                user.getUser().getUserBoost() != null,
                user.getUser().getUserProfile().getWorkType()
        );
    }
}
