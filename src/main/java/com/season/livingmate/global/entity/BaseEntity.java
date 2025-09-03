package com.season.livingmate.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속 할 경우 createdDate, modifiedDate 두 필드도 컬럼으로 인식하도록 설정
@EntityListeners(AuditingEntityListener.class) //BaseTimeEntity 클래스에 Auditing 기능을 포함
public abstract class BaseEntity {

    @CreatedDate // 생성 시 날짜 자동 생성
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시 날짜 자동 생성
    private LocalDateTime modifiedAt;
}
