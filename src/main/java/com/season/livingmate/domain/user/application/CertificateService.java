package com.season.livingmate.domain.user.application;

import com.season.livingmate.domain.auth.security.CustomUserDetails;
import com.season.livingmate.domain.user.domain.UserProfile;
import com.season.livingmate.domain.user.domain.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final UserProfileRepository userProfileRepository;

    // 증명서 조회
    @Transactional(readOnly = true)
    public String getCertificate(CustomUserDetails userDetails) {

        UserProfile userProfile = userProfileRepository.findByUser(userDetails.getUser())
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));

        return userProfile.getCertificateImageUrl();
    }
}
