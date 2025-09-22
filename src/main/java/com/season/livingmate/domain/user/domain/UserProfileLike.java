package com.season.livingmate.domain.user.domain;

import com.season.livingmate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 좋아요를 누른 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 좋아요를 받은 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liked_user_id")
    private User likedUser;

    public static UserProfileLike create(User user, User likedUser) {
        return UserProfileLike.builder()
                .user(user)
                .likedUser(likedUser)
                .build();
    }
}
