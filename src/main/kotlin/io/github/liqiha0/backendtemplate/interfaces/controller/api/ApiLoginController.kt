package io.github.liqiha0.backendtemplate.interfaces.controller.api

import io.github.liqiha0.backendtemplate.application.system.UserApplicationService
import io.github.liqiha0.backendtemplate.application.system.ADMIN_ROLE_NAME
import io.github.liqiha0.backendtemplate.application.system.WechatService
import io.github.liqiha0.backendtemplate.domain.model.system.*
import io.github.liqiha0.backendtemplate.domain.model.system.smscode.SmsCodeType
import io.github.liqiha0.backendtemplate.domain.service.system.SmsCodeService
import io.github.liqiha0.backendtemplate.domain.service.system.TokenManager
import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@ConditionalOnBean(SmsCodeService::class)
@Tag(name = "用户端/短信登录")
@RestController
@RequestMapping("/api/login")
class ApiSmsLoginController(
    private val userAccountRepository: UserAccountRepository,
    private val userApplicationService: UserApplicationService,
    private val roleRepository: RoleRepository,
    private val tokenManager: TokenManager,
    private val smsCodeService: SmsCodeService
) {
    @Operation(summary = "短信验证码登录")
    @Transactional
    @GetMapping("/sms")
    fun loginBySms(phoneNumber: String, code: String): Token {
        val isSmsCodeMatch = this.smsCodeService.verify(SmsCodeType.LOGIN, phoneNumber, code)
        if (!isSmsCodeMatch) {
            throw BusinessException("验证码错误")
        }
        if (this.userAccountRepository.count() == 0L) {
            // 自动注册系统中的第一个用户为管理员
            val role = this.roleRepository.findOne(displayNameEqual(ADMIN_ROLE_NAME)).getOrNull()
                ?: throw BusinessException("未初始化默认管理员角色")
            val account = this.userApplicationService.createUser("管理员", phoneNumber, role.id, false)
            return this.tokenManager.createToken(account.id)
        } else {
            val account = this.userAccountRepository.findOne(phoneNumberEqual(phoneNumber))
                .orElseThrow { BusinessException("手机号不存在") }
            if (account.isDisabled) throw BusinessException("用户已被封禁")
            return this.tokenManager.createToken(account.id)
        }
    }
}

@ConditionalOnBean(WechatService::class)
@RestController
@RequestMapping("/api/login")
@Tag(name = "用户端/微信登录")
class ApiWechatLoginController(
    private val wechatService: WechatService,
    private val tokenManager: TokenManager
) {
    @GetMapping("/wechat")
    @Operation(summary = "微信登录")
    fun loginWithWeChat(code: String, phoneCode: String): Map<String, Any> {
        val account = this.wechatService.findOrRegisterUser(code, phoneCode)
        val token = this.tokenManager.createToken(account.id)
        return mapOf<String, Any>("token" to token.accessToken)
    }
}