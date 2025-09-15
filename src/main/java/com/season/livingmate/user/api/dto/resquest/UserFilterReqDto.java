package com.season.livingmate.user.api.dto.resquest;

import com.season.livingmate.user.domain.CountRange;
import com.season.livingmate.user.domain.SensitivityLevel;
import lombok.Getter;

import java.util.List;

@Getter
public class UserFilterReqDto {
    private boolean smoking; // 흡연 여부
    private CountRange alcoholCount; // 음주 횟수
    private SensitivityLevel sleepLevel; // 잠귀 민감도
    private List<String> pet;
    private SensitivityLevel tidinessLevel; // 정리정돈 성향

}
