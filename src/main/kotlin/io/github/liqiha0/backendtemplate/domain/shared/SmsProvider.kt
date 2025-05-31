package io.github.liqiha0.backendtemplate.domain.shared

interface SmsProvider {
    fun sendCode(phoneNumber: String, code: String)
}