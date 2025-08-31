package com.season.livingmate.auth.entity;

import com.season.livingmate.user.domain.User;
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
    private User user;
}
