package io.github.liqiha0.template.verification.application.service

import io.github.liqiha0.template.core.domain.shared.BusinessException
import io.github.liqiha0.template.verification.domain.model.otp.OneTimePassword
import io.github.liqiha0.template.verification.domain.model.otp.OtpProvider
import io.github.liqiha0.template.verification.domain.model.otp.OtpRepository
import io.github.liqiha0.template.verification.domain.model.otp.OtpType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class VerificationService(
    private val otpRepository: OtpRepository,
    private val otpProviders: List<OtpProvider>
) {

    @Transactional
    fun send(type: OtpType, scene: String, recipient: String) {
        val latestOtp = otpRepository.findFirstByRecipientAndSceneAndTypeOrderByCreatedDateDesc(recipient, scene, type)
        if (latestOtp != null && ZonedDateTime.now().minusMinutes(1).isBefore(latestOtp.createdDate)) {
            throw BusinessException("操作过于频繁，请稍后再试")
        }

        val otp = OneTimePassword(type, scene, recipient)
        otpRepository.save(otp)

        val provider = otpProviders.find { it.supports(type) }
            ?: throw IllegalStateException("No OtpProvider found for type: $type")

        provider.send(recipient, otp.code)
    }

    @Transactional
    fun verify(type: OtpType, scene: String, recipient: String, code: String): Boolean {
        val otp = otpRepository.findFirstByRecipientAndSceneAndTypeOrderByCreatedDateDesc(recipient, scene, type)
            ?: return false

        if (otp.isStillValid() && otp.code == code) {
            otpRepository.delete(otp)
            return true
        } else {
            return false
        }
    }
}
