package com.season.livingmate.domain.user.application;

import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.userProfile.domain.entity.UserProfile;
import com.season.livingmate.domain.user.api.dto.request.LifeRhythmReq;
import com.season.livingmate.domain.user.api.dto.request.SignupReq;
import com.season.livingmate.domain.user.api.dto.response.LifeRhythmRes;
import com.season.livingmate.domain.user.api.dto.response.SignupRes;
import com.season.livingmate.global.auth.application.SmsService;
import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.global.auth.security.JwtProvider;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;

import com.season.livingmate.domain.userProfile.domain.repository.UserProfileRepository;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final Map<String, SignupReq> tempSignupStore = new ConcurrentHashMap<>();
    private final Map<String, SmsService.OtpInfo> otpStore= new ConcurrentHashMap<>();
    private final JwtProvider jwtProvider;

    // 회원가입
    @Transactional
    public void signup(SignupReq signupReqDto){
        validateDuplicateUsername(signupReqDto.getUsername());
        validateDuplicateNickname(signupReqDto.getNickname());
    }

    //유저 정보 db에 저장
    @Transactional
    public SignupRes completeSignup(String phone) {

        SignupReq tempReq = tempSignupStore.get(phone);
        if (tempReq == null) throw new CustomException(ErrorStatus.RESOURCE_NOT_FOUND);

        String encodedPassword = passwordEncoder.encode(tempReq.getPassword());
        User user = tempReq.toEntity(encodedPassword);

        userRepository.save(user);

      // JWT 토큰 발급
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        tempSignupStore.remove(phone);
        otpStore.remove(phone);
        return SignupRes.from(user, accessToken, refreshToken);
    }

    @Transactional
    public LifeRhythmRes createLifeRhythm(LifeRhythmReq dto, CustomUserDetails userDetails) throws Exception {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        UserProfile updatedProfile = dto.toEntity(dto,user);
        UserProfile savedProfile = userProfileRepository.save(updatedProfile);

        return LifeRhythmRes.from(savedProfile, user);
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

}
