package com.season.livingmate.domain.user.api.dto.request;

import com.season.livingmate.domain.userProfile.domain.entity.enums.AlarmCount;


import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.userProfile.domain.entity.UserProfile;
import com.season.livingmate.domain.userProfile.domain.entity.enums.WorkType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class LifeRhythmReq {

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

    public UserProfile toEntity(LifeRhythmReq dto, User user) {
        WorkType workType = dto.getWorkType();
        List<String> workDays = dto.getWorkDays();
        String wakeUpTimeWorkday = dto.getWakeUpTimeWorkday();
        String goWorkTime = dto.getGoWorkTime();
        String comeHomeTime = dto.getComeHomeTime();
        String sleepTimeWorkday = dto.getSleepTimeWorkday();
        String wakeUpTimeHoliday = dto.getWakeUpTimeHoliday();
        String sleepTimeHoliday = dto.getSleepTimeHoliday();
        AlarmCount alarmCount = dto.getAlarmCount();

        return UserProfile.builder()
                .user(user)
               .workType(workType)
                .workDays(workDays)
                .wakeUpTimeWorkday(wakeUpTimeWorkday)
                .goWorkTime(goWorkTime)
                .comeHomeTime(comeHomeTime)
                .sleepTimeWorkday(sleepTimeWorkday)
                .wakeUpTimeHoliday(wakeUpTimeHoliday)
                .sleepTimeHoliday(sleepTimeHoliday)
                .alarmCount(alarmCount)
               .build();
    }

}
