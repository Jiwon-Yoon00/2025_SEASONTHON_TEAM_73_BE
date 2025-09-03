package com.season.livingmate.geo.support;

import com.season.livingmate.geo.api.dto.KakaoAddressRes;

public final class RegionLabelSeoul {
    private RegionLabelSeoul() {}

    // 서울시 구 동 라벨 생성
    public static String toSeoulGuDong(KakaoAddressRes.Document d){
        String r1 = null, r2 = null, r3 = null;
        if(d != null && d.roadAddress() != null){
            r1 = d.roadAddress().region1depthName();
            r2 = d.roadAddress().region2depthName();
            r3 = d.roadAddress().region3depthName();
        }
        if(isBlank(r2) && d != null && d.address() != null){ // 도로명 주소 없으면 지명으로
            r1 = d.address().region1depthName();
            r2 = d.address().region2depthName();
            r3 = d.address().region3depthName();
        }

        // 서울만 지원
        if(!isSeoul(r1)){
            return "";
        }
        return String.format("%s %s %s", "서울시", safe(r2), safe(r3)).trim();
    }

    private static boolean isSeoul(String s){
        if(s == null) return false;
        s = s.trim();
        return s.equals("서울") || s.equals("서울시") || s.equals("서울특별시");
    }
    private static boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s){
        return s == null ? "" : s.trim();
    }

}
