package com.season.livingmate.domain.auth.entity;

import com.season.livingmate.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    private Long expiredAt;

    private boolean revoked; // 사실상 안씀

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void revoke() {
        this.revoked = true;
    }
}
