package com.season.livingmate.global.auth.application;

import com.season.livingmate.domain.user.api.dto.request.SignupReq;
import com.season.livingmate.domain.user.api.dto.request.VerifyOtpReq;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SmsService {

    private DefaultMessageService messageService;
    private final Map<String, SmsService.OtpInfo> otpStore = new ConcurrentHashMap<>();
    private final Map<String, SignupReq> tempSignupStore = new ConcurrentHashMap<>();

    @Value("${solapi.apiKey}")
    private String apiKey;

    @Value("${solapi.secretkey}")
    private String apiSecret;

    // OTP 유효시간 3분
    private static final long OTP_VALID_DURATION = 3 * 60 * 1000;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.solapi.com"
        );
    }

    // OTP 발송
    @Transactional
    public void sendOtp(String phone){
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));

        Message message = new Message();
        message.setFrom("01083548871");
        message.setTo(phone);
        message.setText("[인증번호] " + otp + "를 입력해주세요.");

        try {
            this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.OTP_SEND_FAILED);
        }

        // OTP와 만료시간 저장
        otpStore.put(phone, new SmsService.OtpInfo(otp, System.currentTimeMillis() + OTP_VALID_DURATION));
    }

    // OTP 검증
    @Transactional
    public void verifyOtp(VerifyOtpReq dto){
        String phone = dto.getPhoneNumber();
        OtpInfo info = otpStore.get(phone);

        if (info == null) throw new CustomException(ErrorStatus.OTP_NOT_FOUND); // OTP 정보 조회

        if (phone == null) throw new CustomException(ErrorStatus.RESOURCE_NOT_FOUND); // 임시 회원가입 정보 조회

        if (System.currentTimeMillis() > info.expireAt())
            throw new CustomException(ErrorStatus.OTP_EXPIRED); // OPT 만료시간

        if (!info.otp().equals(dto.getCode())) throw new CustomException(ErrorStatus.OTP_MISMATCH); // OTP 불일치
    }

    // OTP 정보 record
    public record OtpInfo(String otp, long expireAt) {}
}
