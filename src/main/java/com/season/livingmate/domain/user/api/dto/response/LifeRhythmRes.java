package com.season.livingmate.domain.user.api.dto.response;

import com.season.livingmate.domain.userProfile.domain.entity.enums.AlarmCount;

import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.userProfile.domain.entity.UserProfile;
import com.season.livingmate.domain.userProfile.domain.entity.enums.WorkType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LifeRhythmRes {

    @Schema(description = "유저 아이디", example = "1")
    private Long userId;

    @Schema(description = "통근 유형", example = "OFFICE", allowableValues = {"OFFICE", "STUDENT", "REMOTE", "FREELANCER", "UNEMPLOYED"})
    private WorkType workType;

    @ArraySchema(schema = @Schema(description = "출근 요일 목록", example = "월,화,수,목,금"))
    private List<String> workDays;

    @Schema(description = "출근일 기상 시간", example = "07:00")
    private String wakeUpTimeWorkday;

    @Schema(description = "출근 시간", example = "09:00")
    private String goWorkTime;

    @Schema(description = "귀가 시간", example = "20:00")
    private String comeHomeTime;

    @Schema(description = "출근일 취침 시간", example = "23:00")
    private String sleepTimeWorkday;

    @Schema(description = "휴일 기상 시간", example = "08:00")
    private String wakeUpTimeHoliday;

    @Schema(description = "휴일 취침 시간", example = "24:00")
    private String sleepTimeHoliday;

    @Schema(description = "알람 듣는 횟수", example = "ONE", allowableValues = {"ONCE", "TWICE", "THREE_OR_MORE"})
    private AlarmCount alarmCount;

    public static LifeRhythmRes from(UserProfile reqDto, User user) {
        return LifeRhythmRes.builder()
                .userId(user.getId())
                .workType(reqDto.getWorkType())
                .workDays(reqDto.getWorkDays())
                .wakeUpTimeWorkday(reqDto.getWakeUpTimeWorkday())
                .goWorkTime(reqDto.getGoWorkTime())
                .comeHomeTime(reqDto.getComeHomeTime())
                .sleepTimeWorkday(reqDto.getSleepTimeWorkday())
                .wakeUpTimeHoliday(reqDto.getWakeUpTimeHoliday())
                .sleepTimeHoliday(reqDto.getSleepTimeHoliday())
                .alarmCount(reqDto.getAlarmCount())
                .build();
    }
}
