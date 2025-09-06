package com.season.livingmate.post.domain;

import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Embedded
    private GeoPoint geoPoint;

    @Column(nullable = false)
    private String location; // 실제 주소

    @Column(name = "region_label", nullable = false)
    private String regionLabel;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRoom = new ArrayList<>();

    @Column(name = "preferred_gender")
    private String preferredGender;

    @Builder
    private Post(String title,
                 String content,
                 List<String> imageUrls,
                 GeoPoint geoPoint,
                 String location,
                 String regionLabel,
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
                 String preferredGender,
                 LocalDateTime createdAt,
                 User user) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
        this.geoPoint = geoPoint;
        this.location = location;
        this.regionLabel = regionLabel;
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
        this.preferredGender = preferredGender;
        this.createdAt = createdAt;
        this.user = user;
    }

    public void update(String title,
                       String content,
                       List<String> imageUrls,
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
                       String preferredGender
    ) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
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
        this.preferredGender = preferredGender;
        this.updatedAt = LocalDateTime.now();
    }

    public void applyRegionLabel(String label) {
        this.regionLabel = label;
    }

    public String getRegionLabel() {
        return regionLabel;
    }
}
