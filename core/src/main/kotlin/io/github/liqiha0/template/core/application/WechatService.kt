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
    private val principalRepository: PrincipalRepository,
    private val principalFactory: PrincipalFactory
) {

    @Transactional
    fun findOrRegisterUserWithPhone(appName: String, code: String, phoneCode: String): Principal {
        val phoneInfo = this.wxMaMultiServices.getWxMaService(appName).userService.getPhoneNumber(phoneCode)
        val result = this.wxMaMultiServices.getWxMaService(appName).jsCode2SessionInfo(code)
        var account = this.principalRepository.findOne(PrincipalSpecifications.hasPhone(phoneInfo.purePhoneNumber))
            .getOrNull()
        if (account == null) {
            account = this.principalFactory.createWithPhone(phoneInfo.purePhoneNumber)
            account.identities.add(WechatIdentity(result.openid, result.unionid))
            this.principalRepository.save(account)
        }

        return account
    }
}