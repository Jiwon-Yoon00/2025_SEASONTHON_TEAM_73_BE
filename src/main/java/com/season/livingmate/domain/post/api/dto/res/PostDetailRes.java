package com.season.livingmate.domain.post.api.dto.res;

import com.season.livingmate.domain.post.domain.HeatingType;
import com.season.livingmate.domain.post.domain.PaymentStructure;
import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.user.domain.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Schema(description = "게시글 상세 조회 응답 DTO")
public record PostDetailRes(

        Long id,

        String title,

        String content,

        String imageUrl,

        Double latitude,

        Double longitude,

        String location,

        String region,

        String roomType,

        int deposit,

        int monthlyRent,

        int maintenanceFee,

        // 분담(불리언 4개)
        boolean depositShare,

        boolean rentShare,

        boolean maintenanceShare,

        boolean utilitiesShare,

        int floor,

        int buildingFloor,

        int areaSize,

        HeatingType heatingType,

        boolean hasElevator,

        LocalDateTime availableDate,

        int minStayMonths,

        int maxStayMonths,

        int washroomCount,

        int roomCount,

        List<String> preferredGender,

        Long userId,

        String userName,

        String userProfileImageUrl, // 유저 프로필 이미지 추가

        WorkType isCertified,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {

        public static PostDetailRes from(Post p) {
                Double lat = (p.getGeoPoint() != null) ? p.getGeoPoint().getLatitude() : null;
                Double lng = (p.getGeoPoint() != null) ? p.getGeoPoint().getLongitude() : null;

                PaymentStructure ps = p.getPaymentStructure();
                boolean depositShare = ps != null && ps.isDepositShare();
                boolean rentShare = ps != null && ps.isRentShare();
                boolean maintenanceShare = ps != null && ps.isMaintenanceShare();
                boolean utilitiesShare = ps != null && ps.isUtilitiesShare();

                List<String> genders = p.getPreferredGender() != null ?
                        Arrays.asList(p.getPreferredGender().split(",")) :
                        List.of();

                Long userId = p.getUser().getId();
                String userName = p.getUser().getNickname();
                String userProfileImageUrl = p.getUser().getUserProfile() != null ? p.getUser().getUserProfile().getProfileImageUrl() : null;
                WorkType isCertified = p.getUser().getWorkType();

                return new PostDetailRes(
                        p.getPostId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getImageUrl(),
                        lat,
                        lng,
                        p.getLocation(), // 보관용
                        p.getRegionLabel(), // 서울시 구 동
                        p.getRoomType().getKoreanName(),
                        p.getDeposit(),
                        p.getMonthlyRent(),
                        p.getMaintenanceFee(),
                        depositShare,
                        rentShare,
                        maintenanceShare,
                        utilitiesShare,
                        p.getFloor(),
                        p.getBuildingFloor(),
                        p.getAreaSize(),
                        p.getHeatingType(),
                        p.isHasElevator(),
                        p.getAvailableDate(),
                        p.getMinStayMonths(),
                        p.getMaxStayMonths(),
                        p.getWashroomCount(),
                        p.getRoomCount(),
                        genders,
                        userId,
                        userName,
                        userProfileImageUrl,
                        isCertified,
                        p.getCreatedAt(),
                        p.getUpdatedAt()
                );
        }
}
