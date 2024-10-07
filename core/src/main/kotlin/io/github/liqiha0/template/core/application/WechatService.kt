package io.github.liqiha0.template.core.application

import com.binarywang.spring.starter.wxjava.miniapp.service.WxMaMultiServices
import io.github.liqiha0.template.core.domain.model.iam.PhoneIdentity
import io.github.liqiha0.template.core.domain.model.iam.Principal
import io.github.liqiha0.template.core.domain.model.iam.WechatIdentity
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.specification.PrincipalSpecification
import me.chanjar.weixin.mp.api.WxMpService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class WechatService(
    private val wxMaMultiServices: WxMaMultiServices,
    private val wxMpService: WxMpService,
    private val principalRepository: PrincipalRepository,
) {

    @Transactional
    fun findOrRegisterUserWithPhone(
        appName: String,
        code: String,
        phoneCode: String,
        realm: String? = null
    ): Principal {
        val phoneInfo = this.wxMaMultiServices.getWxMaService(appName).userService.getPhoneNumber(phoneCode)
        val result = this.wxMaMultiServices.getWxMaService(appName).jsCode2SessionInfo(code)
        var account = if (!result.unionid.isNullOrBlank()) {
            this.principalRepository.findOne(
                PrincipalSpecification.hasIdentity(WechatIdentity::class, "unionId", result.unionid as String, realm)
            ).getOrNull()
        } else null

        if (account == null) {
            account = this.principalRepository.findOne(
                PrincipalSpecification.hasIdentity(WechatIdentity::class, "openId", result.openid, realm)
            ).getOrNull()
        }

        if (account == null) {
            account = this.principalRepository.findOne(
                PrincipalSpecification.hasIdentity(PhoneIdentity::class, "phone", phoneInfo.purePhoneNumber, realm)
            ).getOrNull()
        }

        if (account == null) {
            account = Principal(
                realm = realm,
                identities = mutableSetOf(
                    PhoneIdentity(phoneInfo.purePhoneNumber),
                    WechatIdentity(result.openid, result.unionid)
                )
            )
            this.principalRepository.save(account)
        } else {
            val wechatIdentities = account.identities.filterIsInstance<WechatIdentity>()
                .filter { it.openId == result.openid }
            if (wechatIdentities.isNotEmpty()) {
                wechatIdentities.forEach { it.unionId = result.unionid }
            } else {
                account.addIdentity(WechatIdentity(result.openid, result.unionid))
            }

            val phoneIdentities = account.identities.filterIsInstance<PhoneIdentity>()
            if (phoneIdentities.isEmpty()) {
                account.addIdentity(PhoneIdentity(phoneInfo.purePhoneNumber))
            }

            this.principalRepository.save(account)
        }

        return account
    }

    @Transactional
    fun findOrRegisterByOauth2(code: String, realm: String? = null): Principal {
        val result = this.wxMpService.oAuth2Service.getAccessToken(code)
        val appId = this.wxMpService.wxMpConfigStorage.appId

        var account = if (!result.unionId.isNullOrBlank()) {
            this.principalRepository.findOne(
                PrincipalSpecification.hasIdentity(WechatIdentity::class, "unionId", result.unionId as String, realm)
            ).getOrNull()
        } else null

        if (account == null) {
            account = this.principalRepository.findOne(
                PrincipalSpecification.hasIdentity(WechatIdentity::class, "openId", result.openId, realm)
            ).getOrNull()
        }

        if (account == null) {
            account = Principal(
                realm = realm,
                identities = mutableSetOf(WechatIdentity(result.openId, result.unionId, appId))
            )
            this.principalRepository.save(account)
        } else {
            val wechatIdentities = account.identities.filterIsInstance<WechatIdentity>()
            val wechatIdentity = wechatIdentities.firstOrNull { it.openId == result.openId }
            if (wechatIdentity != null) {
                wechatIdentity.unionId = result.unionId
                wechatIdentity.appId = appId
                this.principalRepository.save(account)
            } else {
                account.addIdentity(WechatIdentity(result.openId, result.unionId, appId))
                this.principalRepository.save(account)
            }
        }

        return account
    }
}
