package com.kakaoinsurance.payment.adapter.out.persistence;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
class UpdatedEntity extends CreatedEntity {

    @LastModifiedBy
    private Long updateBy;

    @LastModifiedDate
    private LocalDateTime updateDate;

}
