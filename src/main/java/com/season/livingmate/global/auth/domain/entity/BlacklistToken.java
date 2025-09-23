package com.season.livingmate.global.auth.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String blacklistToken;

    private BlacklistReason reason;

    private Long expiredAt;

    private boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private com.season.livingmate.domain.user.domain.entity.User user;
}
