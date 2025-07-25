package io.github.liqiha0.template.verification.domain.service

import io.github.liqiha0.template.domain.model.system.smscode.SmsCode
import io.github.liqiha0.template.domain.model.system.smscode.SmsCodeRepository
import io.github.liqiha0.template.domain.model.system.smscode.SmsCodeType
import io.github.liqiha0.template.domain.shared.SmsProvider
import io.github.liqiha0.template.core.domain.shared.BusinessException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@ConditionalOnBean(SmsProvider::class)
@Service
class SmsCodeService(
    private val smsCodeRepository: SmsCodeRepository,
    private val smsProvider: SmsProvider
) {

    @Transactional
    fun send(type: SmsCodeType, phoneNumber: String) {
        val smsCode = this.smsCodeRepository.findFirstByPhoneAndTypeOrderByCreatedDateDesc(phoneNumber, type)
        if (smsCode != null && ZonedDateTime.now().minusMinutes(1).isBefore(smsCode.createdDate)) {
            throw BusinessException("操作过于频繁，请稍后再试")
        }
        val codeEntity = this.smsCodeRepository.save(SmsCode(phoneNumber, type))
        this.smsProvider.sendCode(phoneNumber, codeEntity.code)
    }

    @Transactional
    fun verify(type: SmsCodeType, phoneNumber: String, code: String): Boolean {
        val smsCode = this.smsCodeRepository.findFirstByPhoneAndTypeOrderByCreatedDateDesc(phoneNumber, type)
            ?: return false
        if (!smsCode.isExpired() && smsCode.code == code) {
            this.smsCodeRepository.delete(smsCode)
            return true
        } else {
            return false
        }
    }
}
