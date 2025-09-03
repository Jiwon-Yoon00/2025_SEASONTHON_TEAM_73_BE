package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.entity.Gender;
import com.season.livingmate.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "지도 마커 클릭 시 표시할 게시글 상세 정보")
public record PostMapDetailRes(
        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "지역(서울시 구 동)", example = "서울시 노원구 중계동")
        String region,

        @Schema(description = "방 개수", example = "1")
        int roomCount,

        @Schema(description = "화장실 개수", example = "1")
        int washroomCount,

        @Schema(description = "월세(만원)", example = "50")
        int monthlyRent,

        @Schema(description = "보증금(만원)", example = "1000")
        int deposit,

        @Schema(description = "입주 가능일", example = "2025-09-10T00:00:00")
        LocalDateTime availableDate,

        @Schema(description = "최소 계약 기간(개월)", example = "6")
        int minStayMonths,

        // User 정보
        @Schema(description = "게시자 닉네임", example = "홍길동")
        String userNickname,

        @Schema(description = "게시자 나이", example = "25")
        int userAge,

        @Schema(description = "게시자 성별", example = "MALE")
        Gender userGender,

        @Schema(description = "방 구하는 사람인지 여부", example = "true")
        boolean isRoom,

        // 어딨지
        @Schema(description = "출근 요일", example = "월,화,수,목,금")
        String workDays,

        @Schema(description = "출근 시간", example = "09:00")
        String workTime,

        @Schema(description = "흡연 여부", example = "false")
        Boolean isSmoking
) {
    public static PostMapDetailRes from(Post post) {
        User user = post.getUser();
        
        return new PostMapDetailRes(
                post.getPostId(),
                post.getTitle(),
                post.getRegionLabel(),
                post.getRoomCount(),
                post.getWashroomCount(),
                post.getMonthlyRent(),
                post.getDeposit(),
                post.getAvailableDate(),
                post.getMinStayMonths(),
                user.getNickname(),
                user.getAge(),
                user.getGender(),
                user.isRoom(),
                // 어딨지
                null, // workDays
                null, // workTime
                null  // isSmoking
        );
    }
}
