package com.season.livingmate.auth.service;

import com.season.livingmate.auth.dto.request.LifeRhythmReqDto;
import com.season.livingmate.auth.dto.request.SignupReqDto;
import com.season.livingmate.auth.dto.request.VerifyOtpReqDto;
import com.season.livingmate.auth.dto.response.LifeRhythmResDto;
import com.season.livingmate.auth.dto.response.SignupResDto;
import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.auth.security.JwtProvider;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.api.dto.response.UserProfileResDto;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserProfileRepository;
import com.season.livingmate.user.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private DefaultMessageService messageService;
    private final Map<String, SignupService.OtpInfo> otpStore = new ConcurrentHashMap<>();
    private final Map<String, SignupReqDto> tempSignupStore = new ConcurrentHashMap<>();
    private final JwtProvider jwtProvider;

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

    @Transactional
    public void signup(SignupReqDto signupReqDto){
        validateDuplicateUsername(signupReqDto.getUsername());
        validateDuplicateNickname(signupReqDto.getNickname());

        SignupReqDto encryptedDto = new SignupReqDto();
        encryptedDto.setUsername(signupReqDto.getUsername());
        encryptedDto.setNickname(signupReqDto.getNickname());
        encryptedDto.setGender(signupReqDto.getGender());
        encryptedDto.setAge(signupReqDto.getAge());
        encryptedDto.setRoom(signupReqDto.isRoom());
        encryptedDto.setPassword(passwordEncoder.encode(signupReqDto.getPassword()));
        encryptedDto.setConfirmPassword(null); // 검증 후 필요 없음

        tempSignupStore.put(encryptedDto.getUsername(), encryptedDto);

    }

    // 아이디 중복 검사
    public void validateDuplicateUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorStatus.DUPLICATE_RESOURCE);
        }
    }

    // 닉네임 중복 검사
    public void validateDuplicateNickname(String nickname) {
        if(userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorStatus.DUPLICATE_RESOURCE);
        }
    }

    // OTP 발송
    @Transactional
    public void sendOtp(String phone) throws Exception {
        String otp = String.valueOf((int)((Math.random() * 900000) + 100000));

        Message message = new Message();
        message.setFrom("01083548871");
        message.setTo(phone);
        message.setText("[인증번호] " + otp + "를 입력해주세요.");

        try{
            this.messageService.sendOne(new SingleMessageSendingRequest(message));
        }catch(Exception e ){
            throw new CustomException(ErrorStatus.OTP_SEND_FAILED);
        }

        // OTP와 만료시간 저장
        otpStore.put(phone, new SignupService.OtpInfo(otp, System.currentTimeMillis() + OTP_VALID_DURATION));
    }

    // OTP 검증 후 유저 정보 db에 저장
    @Transactional
    public SignupResDto verifyOtp(VerifyOtpReqDto dto) throws Exception {
        String phone = dto.getPhoneNumber();
        String username = dto.getUsername();
        SignupService.OtpInfo info = otpStore.get(dto.getPhoneNumber());


        if (info == null) throw new CustomException(ErrorStatus.OTP_NOT_FOUND); // OTP 정보 조회

        SignupReqDto signupInfo = tempSignupStore.get(username);
        if (phone == null) throw new CustomException(ErrorStatus.RESOURCE_NOT_FOUND); // 임시 회원가입 정보 조회

        if (System.currentTimeMillis() > info.expireAt()) throw new CustomException(ErrorStatus.OTP_EXPIRED); // OPT 만료시간
        if (!info.otp().equals(dto.getCode())) throw new CustomException(ErrorStatus.OTP_MISMATCH);


        otpStore.remove(phone);
        tempSignupStore.remove(phone);

        User user = User.builder()
                .username(signupInfo.getUsername())
                .nickname(signupInfo.getNickname())
                .password(signupInfo.getPassword()) // 이미 암호화된 비밀번호
                .gender(signupInfo.getGender())
                .age(signupInfo.getAge())
                .isRoom(signupInfo.isRoom())
                .verified(true)
                .build();

        userRepository.save(user);

//        // JWT 토큰 발급
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        return SignupResDto.from(user, accessToken, refreshToken);
    }

    @Transactional
    public LifeRhythmResDto createLifeRhythm(LifeRhythmReqDto dto, CustomUserDetails userDetails) throws Exception {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile updatedProfile = dto.toEntity(dto,user);
        UserProfile savedProfile = userProfileRepository.save(updatedProfile);

        return LifeRhythmResDto.from(savedProfile, user);
    }


    // OTP 정보 record
    private record OtpInfo(String otp, long expireAt) {}

}
