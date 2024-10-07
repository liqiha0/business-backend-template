package io.github.liqiha0.backendtemplate.application.system

import cn.binarywang.wx.miniapp.api.WxMaService
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccount
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.WechatBinding
import io.github.liqiha0.backendtemplate.domain.model.system.phoneNumberEqual
import io.github.liqiha0.backendtemplate.domain.service.system.AccountService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class WechatService(
    private val userAccountRepository: UserAccountRepository,
    private val wxMaService: WxMaService,
    private val accountService: AccountService
) {

    @Transactional
    fun findOrRegisterUser(code: String, phoneCode: String): UserAccount {
        val phoneInfo = this.wxMaService.userService.getPhoneNoInfo(phoneCode)
        val result = this.wxMaService.jsCode2SessionInfo(code)
        var account = userAccountRepository.findOne(phoneNumberEqual(phoneInfo.purePhoneNumber)).getOrNull()
        if (account == null) {
            account = this.accountService.createUser(phoneInfo.purePhoneNumber, "微信用户")
            account.wechatBinding = WechatBinding(result.openid, result.sessionKey, result.unionid)
            this.userAccountRepository.save(account)
        }

        return account
    }
}