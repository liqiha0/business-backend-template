package io.github.liqiha0.template.core.domain.shared

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableAggregateRoot<T : AuditableAggregateRoot<T>> : AbstractAggregateRoot<T>() {

    @CreatedBy
    @Column(updatable = false)
    @Schema(description = "创建人")
    var createdBy: UUID? = null
        protected set

    @CreatedDate
    @Column(nullable = false)
    @Schema(description = "创建时间")
    lateinit var createdDate: ZonedDateTime
        protected set

    @LastModifiedBy
    @Schema(description = "更新人")
    var lastModifiedBy: UUID? = null
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    @Schema(description = "更新时间")
    lateinit var lastModifiedDate: ZonedDateTime
        protected set
}
