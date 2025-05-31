package io.github.liqiha0.backendtemplate.interfaces.controller.api

import io.github.liqiha0.backendtemplate.domain.model.system.smscode.SmsCodeType
import io.github.liqiha0.backendtemplate.domain.service.system.SmsCodeService
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ConditionalOnBean(SmsCodeService::class)
@RestController
@RequestMapping("/api/sms-code")
@VbenResponse
@Tag(name = "用户端/短信验证码")
class ApiSmsController(
    private val smsCodeService: SmsCodeService
) {
    data class SendSmsCodeRequest(
        val phone: String,
        val type: SmsCodeType = SmsCodeType.LOGIN
    )

    @Operation(summary = "发送验证码")
    @PostMapping
    fun send(@RequestBody request: SendSmsCodeRequest) {
        this.smsCodeService.send(request.type, request.phone)
    }
}
