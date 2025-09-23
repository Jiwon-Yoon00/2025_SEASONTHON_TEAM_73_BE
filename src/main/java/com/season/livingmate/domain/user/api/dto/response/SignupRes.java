package com.season.livingmate.domain.user.api.dto.response;


import com.season.livingmate.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignupRes {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 아이디", example = "testuser")
    private String username;

    @Schema(description = "사용자 닉네임", example = "nickname123")
    private String nickname;

    @Schema(description = "발급된 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "발급된 Refresh Token", example = "dGhpc2lzaXJyYW5kb21zdHJpbmc...")
    private String refreshToken;

    public static SignupRes from(User user, String accessToken, String refreshToken) {
        return new SignupRes(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                accessToken,
                refreshToken
        );
    }
}
