package io.github.liqiha0.template.verification.domain.model.otp

interface OtpProvider {
    fun send(recipient: String, code: String)
    fun supports(type: OtpType): Boolean
}
