package org.example.expert.domain.common.entity

import jakarta.persistence.*
import lombok.Getter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Timestamped {
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private lateinit var modifiedAt: LocalDateTime

    fun getCreatedAt() = this.createdAt
    fun getModifiedAt() = this.modifiedAt
}
