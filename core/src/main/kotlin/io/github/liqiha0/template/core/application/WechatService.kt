package io.github.liqiha0.template.core.application

import com.binarywang.spring.starter.wxjava.miniapp.service.WxMaMultiServices
import io.github.liqiha0.template.core.domain.model.iam.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
//@ConditionalOnBean(WxMaMultiServices::class)
class WechatService(
    private val wxMaMultiServices: WxMaMultiServices,
    private val accountRepository: AccountRepository,
    private val accountFactory: AccountFactory
) {

    @Transactional
    fun findOrRegisterUserWithPhone(appName: String, code: String, phoneCode: String): Account {
        val phoneInfo = this.wxMaMultiServices.getWxMaService(appName).userService.getPhoneNumber(phoneCode)
        val result = this.wxMaMultiServices.getWxMaService(appName).jsCode2SessionInfo(code)
        var account = this.accountRepository.findOne(AccountSpecifications.hasPhone(phoneInfo.purePhoneNumber))
            .getOrNull()
        if (account == null) {
            account = this.accountFactory.createWithPhone(phoneInfo.purePhoneNumber)
            account.credentials.add(WechatCredential(result.openid, result.sessionKey, result.unionid))
            this.accountRepository.save(account)
        }

        return account
    }
}