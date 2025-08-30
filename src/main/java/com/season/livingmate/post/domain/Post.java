package com.season.livingmate.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Embedded
    private GeoPoint geoPoint;

    @Column(nullable = false)
    private String location; // 실제 주소

    // 추후 카카오맵 연결 후 활성화 예정
//    @Column(nullable = false)
//    private String dong; // 동 정보

    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private int deposit;

    @Column(name = "monthly_rent", nullable = false)
    private int monthlyRent;

    @Column(name = "maintenance_fee", nullable = false)
    private int maintenanceFee;

    @Embedded
    private PaymentStructure paymentStructure;

    @Column(nullable = false)
    private int floor;

    @Column(name = "building_floor", nullable = false)
    private int buildingFloor;

    @Column(name = "area_size", nullable = false)
    private int areaSize;

    @Column(name = "heating_type", nullable = false)
    private HeatingType heatingType;

    @Column(nullable = false)
    private boolean hasElevator;

    @Column(name = "available_date", nullable = false)
    private LocalDateTime availableDate;

    @Column(name = "min_stay_months")
    private int minStayMonths;

    @Column(name = "max_stay_months")
    private int maxStayMonths;

    @Column(name = "washroom_count", nullable = false)
    private int washroomCount;

    @Column(name = "room_count", nullable = false)
    private int roomCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Builder
    private Post(String title,
                 String content,
                 String imageUrl,
                 GeoPoint geoPoint,
                 String location,
                 RoomType roomType,
                 int deposit,
                 int monthlyRent,
                 int maintenanceFee,
                 PaymentStructure paymentStructure,
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
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.geoPoint = geoPoint;
        this.location = location;
        this.roomType = roomType;
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
        this.maintenanceFee = maintenanceFee;
        this.paymentStructure = paymentStructure;
        this.floor = floor;
        this.buildingFloor = buildingFloor;
        this.areaSize = areaSize;
        this.heatingType = heatingType;
        this.hasElevator = hasElevator;
        this.availableDate = availableDate;
        this.minStayMonths = minStayMonths;
        this.maxStayMonths = maxStayMonths;
        this.washroomCount = washroomCount;
        this.roomCount = roomCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(String title,
                       String content,
                       String imageUrl,
                       GeoPoint geoPoint,
                       String location,
                       RoomType roomType,
                       int deposit,
                       int monthlyRent,
                       int maintenanceFee,
                       PaymentStructure paymentStructure,
                       int floor,
                       int buildingFloor,
                       int areaSize,
                       HeatingType heatingType,
                       boolean hasElevator,
                       LocalDateTime availableDate,
                       int minStayMonths,
                       int maxStayMonths,
                       int washroomCount,
                       int roomCount) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.geoPoint = geoPoint;
        this.location = location;
        this.roomType = roomType;
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
        this.maintenanceFee = maintenanceFee;
        this.paymentStructure = paymentStructure;
        this.floor = floor;
        this.buildingFloor = buildingFloor;
        this.areaSize = areaSize;
        this.heatingType = heatingType;
        this.hasElevator = hasElevator;
        this.availableDate = availableDate;
        this.minStayMonths = minStayMonths;
        this.maxStayMonths = maxStayMonths;
        this.washroomCount = washroomCount;
        this.roomCount = roomCount;
        this.updatedAt = LocalDateTime.now();
    }
}
