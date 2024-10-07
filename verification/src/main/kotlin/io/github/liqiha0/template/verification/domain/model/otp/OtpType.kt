package io.github.liqiha0.template.verification.domain.model.otp

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "一次性密码类型")
enum class OtpType {
    @Schema(description = "短信")
    SMS,

    @Schema(description = "邮件")
    EMAIL
}