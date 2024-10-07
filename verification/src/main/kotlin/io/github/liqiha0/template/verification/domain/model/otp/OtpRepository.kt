package io.github.liqiha0.template.verification.domain.model.otp

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OtpRepository : JpaRepository<OneTimePassword, UUID> {
    fun findFirstByRecipientAndSceneAndTypeOrderByCreatedDateDesc(recipient: String, scene: String, type: OtpType): OneTimePassword?
}
