package com.season.livingmate.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOtpReqDto {

    @NotBlank(message = "폰번호는 필수입니다.")
    @Schema(description = "사용자 휴대폰 번호", example = "01012345678", required = true)
    private String phoneNumber;
}
