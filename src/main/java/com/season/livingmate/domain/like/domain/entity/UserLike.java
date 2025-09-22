package com.season.livingmate.domain.like.domain.entity;

import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.global.entity.BaseEntity;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLike extends BaseEntity {

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

    public static UserLike create(User user, User likedUser) {
        if(user.getId().equals(likedUser.getId())){
            throw new CustomException(ErrorStatus.SELF_LIKE_NOT_ALLOWED);
        }

        return UserLike.builder()
                .user(user)
                .likedUser(likedUser)
                .build();
    }
}
