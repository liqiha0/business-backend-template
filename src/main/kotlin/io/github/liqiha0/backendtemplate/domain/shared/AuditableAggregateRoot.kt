package io.github.liqiha0.backendtemplate.domain.shared

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
    @Schema(description = "创建人")
    var createdBy: UUID? = null
        internal set

    @CreatedDate
    @Schema(description = "创建时间")
    lateinit var createdDate: ZonedDateTime
        internal set

    @LastModifiedBy
    @Schema(description = "更新人")
    var lastModifiedBy: UUID? = null
        internal set

    @LastModifiedDate
    @Schema(description = "更新时间")
    lateinit var lastModifiedDate: ZonedDateTime
        internal set
}
