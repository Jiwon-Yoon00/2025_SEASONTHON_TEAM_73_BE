package com.season.livingmate.domain.user.api;

import com.season.livingmate.domain.user.api.dto.request.LifeRhythmReq;
import com.season.livingmate.domain.user.api.dto.request.SignupReq;
import com.season.livingmate.domain.user.api.dto.response.LifeRhythmRes;
import com.season.livingmate.domain.user.api.dto.response.SignupRes;
import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.domain.user.application.SignupService;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Signup", description = "SMS 문자인증을 통한 회원가입 API (순서: signup → send-one → verify-otp)")
@RequestMapping("/auth")
public class SignupController {
    private final SignupService signupService;

    @Operation(summary = "회원가입", description = "기본 정보를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Response<SignupRes>> signup(@RequestBody @Valid SignupReq signupReqDto) {
        signupService.signup(signupReqDto);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, null));
    }

    @PostMapping("/signup/complete")
    @Operation(summary = "회원가입 완료", description = "OTP 검증 후 회원가입 버튼 클릭 시 DB에 저장하고 JWT 발급")
    public ResponseEntity<Response<SignupRes>> completeSignup(@RequestBody String phoneNumber) {
        SignupRes res = signupService.completeSignup(phoneNumber);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, res));
    }

    @Operation(summary = "회원 가입 시 생활 리듬만 작성")
    @PostMapping("/life-rhythm")
    public ResponseEntity<Response<LifeRhythmRes>> setLifeRhythm(@RequestBody @Valid LifeRhythmReq lifeRhythmReq, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception{
        LifeRhythmRes dto = signupService.createLifeRhythm(lifeRhythmReq, customUserDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }
}
