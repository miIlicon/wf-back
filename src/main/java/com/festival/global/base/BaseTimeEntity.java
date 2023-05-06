package com.festival.global.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 생성일, 수정일
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modify_date")
    private LocalDateTime modifiedDate;
}