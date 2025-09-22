package com.season.livingmate.domain.geo.application;

import com.season.livingmate.domain.geo.api.dto.GeoRes;
import com.season.livingmate.domain.geo.api.dto.KakaoAddressRes;
import com.season.livingmate.domain.geo.api.dto.RegionCodeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

// 주소 문자열을 카카오 local api로 조회해 표시 주소, 위도, 경도 로 변환
@Service
@RequiredArgsConstructor
public class GeoService {

    private final WebClient kakaoWebClient;

    public Optional<GeoRes> geocodeOne(String query){
        KakaoAddressRes res = kakaoWebClient.get()
                .uri(u -> u.path("/v2/local/search/address.json")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(KakaoAddressRes.class)
                .block();

        if(res == null || res.documents() == null || res.documents().isEmpty()){
            return Optional.empty();
        }

        var d = res.documents().get(0);

        // 도로명 주소가 있으면 우선 없으면 지번 주소
        String display = (d.roadAddress()!=null && d.roadAddress().addressName()!=null)
                ? d.roadAddress().addressName()
                : d.addressName();

        // 좌표 파싱
        Double lat = safeParseDouble(d.y());
        Double lng = safeParseDouble(d.x());
        if (display == null || lat == null || lng == null) {
            return Optional.empty();
        }

        return Optional.of(new GeoRes(display, lat, lng));
    }

    public Optional<KakaoAddressRes.Document> findFirstDocument(String query){
        KakaoAddressRes res = kakaoWebClient.get()
                .uri(u -> u.path("/v2/local/search/address.json")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(KakaoAddressRes.class)
                .block();

        if(res == null || res.documents() == null || res.documents().isEmpty()){
            return Optional.empty();
        }
        return Optional.of(res.documents().get(0));
    }

    // 좌표로 시 구 동 조회해서 라벨 만들기
    public Optional<RegionParts> regionByCoord(double lat, double lng) {
        RegionCodeRes res = kakaoWebClient.get()
                .uri(u -> u.path("/v2/local/geo/coord2regioncode.json")
                        .queryParam("x", lng) // 경도
                        .queryParam("y", lat) // 위도
                        .build())
                .retrieve()
                .bodyToMono(RegionCodeRes.class)
                .block();

        if (res == null || res.documents() == null || res.documents().isEmpty()) {
            return Optional.empty();
        }
        var d = res.documents().get(0);
        String r1 = d.region1depthName();
        String r2 = d.region2depthName();
        String r3 = d.region3depthName();

        if (!isSeoul(r1)) return Optional.empty(); // 정책: 서울만

        String label = "서울시 " + safe(r2) + " " + safe(r3);
        return Optional.of(new RegionParts(label));
    }

    // 시 구 동 라벨 전달
    private static Double safeParseDouble(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Double.valueOf(s); } catch (NumberFormatException e) { return null; }
    }

    public record RegionParts(String regionLabel) {}

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean isSeoul(String s) {
        if (s == null) return false;
        s = s.trim();
        return s.equals("서울") || s.equals("서울시") || s.equals("서울특별시");
    }
}
