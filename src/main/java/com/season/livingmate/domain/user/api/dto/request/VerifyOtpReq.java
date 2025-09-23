package com.season.livingmate.domain.user.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpReq {

    @Schema(description = "사용자 아이디", example = "testuser", required = true)
    private String username;

    @NotBlank(message = "폰번호는 필수입니다.")
    @Schema(description = "사용자 휴대폰 번호", example = "01012345678", required = true)
    String phoneNumber;

    @NotBlank(message = "인증번호는 필수입니다")
    @Schema(description = "전송된 OTP 인증번호", example = "123456", required = true)
    String code;
}
