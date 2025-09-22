package com.season.livingmate.domain.user.domain;

import com.season.livingmate.domain.auth.entity.RefreshToken;
import com.season.livingmate.domain.chat.domain.ChatRoom;
import com.season.livingmate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private boolean isRoom;

    private boolean verified; // 본인인증 완료 여부

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserBoost userBoost;

    @OneToMany(mappedBy = "sender") // 사용자가 보낸 채팅방 목록
    private List<ChatRoom> sentChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "receiver") // 사용자가 받은 채팅방 목록
    private List<ChatRoom> receivedChatRooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WorkType workType; // 증명서 제출 여부

    @Column(nullable = false)
    private boolean isPersonalitySurveyCompleted; // 성향조사 완료 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;
}
