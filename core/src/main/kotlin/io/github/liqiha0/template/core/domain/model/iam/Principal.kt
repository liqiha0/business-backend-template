package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Principal(
    @Type(JsonType::class)
    @Column(nullable = false)
    var roleIds: Set<UUID> = emptySet(),
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn("principal_id")
    var identities: MutableSet<Identity>,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn("principal_id")
    var credentials: MutableSet<Credential>,
    disabled: Boolean = false
) : AuditableAggregateRoot<Principal>() {
    init {
        require(identities.isNotEmpty())
    }

    @Id
    @UuidGenerator
    lateinit var id: UUID
        internal set

    var disabled: Boolean = disabled
        set(value) {
            if (field != value) {
                field = value
                // TODO: 发送领域事件
            }
        }
}

inline fun <reified T : Identity> Principal.getIdentity(): T? {
    return this.identities.find { it is T } as T?
}

inline fun <reified T : Credential> Principal.getCredential(): T? {
    return this.credentials.find { it is T } as T?
}

interface PrincipalRepository : JpaRepository<Principal, UUID>, JpaSpecificationExecutor<Principal>
