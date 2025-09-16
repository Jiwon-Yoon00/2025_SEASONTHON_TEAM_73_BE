package com.season.livingmate.main.api;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.main.api.dto.DashboardResDto;
import com.season.livingmate.main.application.MainDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "메인 대시보드", description = "메인 대시보드 관련 api")
@RequestMapping("/dashboard")
public class MainDashboardController {

    private final MainDashboardService mainDashboardService;

    @Operation(summary = "방 없는 사용자 대시보드 조회")
    @GetMapping("/noroom")
    public ResponseEntity<Response<DashboardResDto>> getNoRoomDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardResDto dashboard = mainDashboardService.getNoRoomDashboard(userDetails.getUser());
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dashboard));
    }

    @Operation(summary = "방 있는 사용자 대시보드 조회")
    @GetMapping("/hasroom")
    public ResponseEntity<Response<DashboardResDto>> getHasRoomDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardResDto dashboard = mainDashboardService.getHasRoomDashboard(userDetails.getUser());
        return ResponseEntity.ok(Response.success(SuccessStatus.SUCCESS, dashboard));
    }
}
