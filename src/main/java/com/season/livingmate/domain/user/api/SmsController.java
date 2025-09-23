package com.season.livingmate.domain.user.api;

import com.season.livingmate.domain.user.api.dto.request.SendOtpReq;
import com.season.livingmate.domain.user.api.dto.request.VerifyOtpReq;
import com.season.livingmate.global.auth.application.SmsService;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send-otp")
    @Operation(summary = "OTP 전송", description = "사용자의 전화번호로 일회용 OTP를 전송합니다.")
    public ResponseEntity<Response<Void>> sendOtp(@RequestBody @Valid SendOtpReq sendOtpReq){
        smsService.sendOtp(sendOtpReq.getPhoneNumber());
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, null));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "OTP 검증", description = "사용자가 입력한 OTP를 검증합니다.")
    public ResponseEntity<Response<Void>> verifyOtp(@RequestBody @Valid VerifyOtpReq verifyOtpReq){
        smsService.verifyOtp(verifyOtpReq);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, null));
    }
}
