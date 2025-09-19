package com.season.livingmate.user.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.repository.UserProfileRepository;
import com.season.livingmate.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
