package io.github.liqiha0.template.core.domain.model.iam

import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    lateinit var id: UUID
        protected set
}

@Entity
class PasswordCredential(
    @Column(nullable = false)
    var passwordHash: String
) : Credential()
