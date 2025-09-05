package com.season.livingmate.auth.controller;

import com.season.livingmate.auth.dto.request.LifeRhythmReqDto;
import com.season.livingmate.auth.dto.request.SendOtpReqDto;
import com.season.livingmate.auth.dto.request.SignupReqDto;
import com.season.livingmate.auth.dto.request.VerifyOtpReqDto;
import com.season.livingmate.auth.dto.response.LifeRhythmResDto;
import com.season.livingmate.auth.dto.response.SignupResDto;
import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.auth.service.SignupService;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Singup", description = "sms문자인증을 통한 회원가입 - singup > send-one > verify-otp")
@RequestMapping("/auth")
public class SignupController {
    private final SignupService signupService;

    @Operation(summary = "회원가입", description = "기본 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<Response<SignupReqDto>> signup(@RequestBody @Valid SignupReqDto signupReqDto){
        try {
            signupService.signup(signupReqDto);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, null));
        }catch (CustomException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.fail(e.getErrorCode()));
        }
    }

    @PostMapping("/send-one")
    @Operation(summary = "OTP 전송", description = "사용자의 전화번호로 일회용 OTP를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP 전송 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
    })
    public ResponseEntity<Response<String>> sendOtp(@RequestBody @Valid SendOtpReqDto sendOtpReqDto) throws Exception {
        try{
            signupService.sendOtp(sendOtpReqDto.getPhoneNumber());
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, null));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Response.fail(e.getErrorCode())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Response.fail(ErrorStatus.INTERNAL_SERVER_ERROR)));
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "OTP 검증", description = "사용자가 입력한 OTP를 검증하고 회원가입을 완료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP 검증 성공 및 회원가입 완료"
            )
    })
    public ResponseEntity<Response<SignupResDto>> verifyOtp(@RequestBody @Valid VerifyOtpReqDto request) throws Exception {
        try{
            SignupResDto signupResDto = signupService.verifyOtp(request);
            return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, signupResDto));
        }catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.fail(e.getErrorCode()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.fail(ErrorStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/life-rhythm")
    @Operation(summary = "회원 가입 시 생활 리듬만 작성")
    public ResponseEntity<Response<LifeRhythmResDto>> setLifeRhythm(@RequestBody @Valid LifeRhythmReqDto lifeRhythmReqDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception{
        LifeRhythmResDto dto = signupService.createLifeRhythm(lifeRhythmReqDto, customUserDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dto));
    }
}
