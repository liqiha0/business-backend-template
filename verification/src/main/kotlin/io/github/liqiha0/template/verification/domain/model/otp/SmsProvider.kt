package io.github.liqiha0.template.verification.domain.model.otp

interface SmsProvider {
    fun sendCode(phoneNumber: String, code: String)
}