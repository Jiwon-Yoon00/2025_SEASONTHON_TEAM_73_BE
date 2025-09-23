package com.season.livingmate.domain.user.api.dto.request;

import com.season.livingmate.domain.user.domain.entity.Gender;
import com.season.livingmate.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class SignupReq {
    @NotBlank(message = "아이디는 필수입니다.")
    @Schema(description = "사용자 아이디", example = "testuser", required = true)
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "사용자 비밀번호", example = "password123", required = true)
    private String password;

    @NotNull
    @Schema(description = "사용자 성별", example = "MALE", required = true)
    private Gender gender;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    @Schema(description = "비밀번호 확인", example = "password123", required = true)
    private String confirmPassword;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Schema(description = "사용자 닉네임", example = "nickname123", required = true)
    private String nickname;

    @Schema(description = "사용자 나이", example = "25")
    private int age;

    @Schema(description = "사용자가 방을 가지고 있는지 여부", example = "true")
    private boolean hasRoom;

    public User toEntity(String encodedPassword){
        return User.builder()
                .nickname(nickname)
                .username(username)
                .password(encodedPassword)
                .gender(gender)
                .age(age)
                .hasRoom(hasRoom)
                .build();
    }
}
