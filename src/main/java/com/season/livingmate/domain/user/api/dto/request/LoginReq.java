package com.season.livingmate.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginReq {

    @NotBlank(message = "아이디는 필수입니다.")
    @Schema(description = "사용자 아이디", example = "testuser", required = true)
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "사용자 비밀번호", example = "password123", required = true)
    private String password;
}
