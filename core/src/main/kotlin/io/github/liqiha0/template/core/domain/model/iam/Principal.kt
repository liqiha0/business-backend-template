package io.github.liqiha0.template.core.domain.model.iam

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.hibernate.annotations.Type
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class Principal(
    @Type(JsonType::class)
    @Column(columnDefinition = "jsonb", nullable = false)
    var roleIds: Set<UUID> = emptySet(),
    identities: Set<Identity>,
    credentials: Set<Credential> = emptySet(),
    realm: String? = null,
    disabled: Boolean = false
) : AuditableAggregateRoot<Principal>() {

    init {
        require(identities.isNotEmpty())
    }

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "principal_id")
    protected var _identities = identities.toMutableSet()
    val identities: Set<Identity> get() = this._identities

    init {
        _identities.forEach { it.realm = realm }
    }

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "principal_id")
    private var _credentials = credentials.toMutableSet()
    val credentials: Set<Credential> get() = this._credentials

    @Column(nullable = true)
    var realm = realm
        set(value) {
            field = value
            this._identities.forEach { it.realm = value }
        }

    @Id
    @UuidGenerator
    @Column(nullable = false)
    lateinit var id: UUID
        protected set

    @Column(nullable = false)
    var disabled: Boolean = disabled
        set(value) {
            if (field != value) {
                field = value
                if (this::id.isInitialized) {
                    val event = if (value) {
                        PrincipalDisabledEvent(this.id)
                    } else {
                        PrincipalEnabledEvent(this.id)
                    }
                    this.registerEvent(event)
                }
            }
        }

    fun addIdentity(identity: Identity) {
        identity.realm = this.realm
        this._identities.add(identity)
    }

    fun addCredential(credential: Credential) {
        this._credentials.add(credential)
    }

    @PrePersist
    fun onPrePersist() {
        this._identities.forEach { it.realm = this.realm }
    }

    @PreUpdate
    fun onPreUpdate() {
        this._identities.forEach { it.realm = this.realm }
    }
}

inline fun <reified T : Identity> Principal.getIdentity(): T? =
    this.identities.find { it is T } as T?

inline fun <reified T : Identity> Principal.getIdentity(predicate: (T) -> Boolean): T? =
    this.identities.find { it is T && predicate(it as T) } as T?

inline fun <reified T : Credential> Principal.getCredential(): T? =
    this.credentials.find { it is T } as T?

inline fun <reified T : Credential> Principal.getCredential(predicate: (T) -> Boolean): T? =
    this.credentials.find { it is T && predicate(it as T) } as T?

class PrincipalDisabledEvent(val principalId: UUID)
class PrincipalEnabledEvent(val principalId: UUID)
