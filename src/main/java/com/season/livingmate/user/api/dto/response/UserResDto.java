package com.season.livingmate.user.api.dto.response;

import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Schema(description = "유저프로필 응답 DTO")
@Getter
public class UserResDto {

    @Schema(description = "유저 ID", example = "1")
    private Long id;

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

    public static UserResDto from(User user) {
        return new UserResDto(
                user.getId(),
                user.getNickname(),
                user.getAge(),
                user.isVerified(),
                user.getGender(),
                user.isCertified(),
                user.isRoom(),
                user.getUserProfile().isSmoking()
        );
    }
}
