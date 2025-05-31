package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.UserAccount
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccountRepository
import io.github.liqiha0.backendtemplate.domain.service.system.AccountService
import io.github.liqiha0.backendtemplate.domain.service.system.UserAccountService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserApplicationService(
    private val userAccountService: UserAccountService,
    private val userAccountRepository: UserAccountRepository,
    private val accountService: AccountService
) {

    @Transactional
    fun createUser(displayName: String, phoneNumber: String, roleId: UUID, isDisabled: Boolean): UserAccount {
        val account = this.userAccountService.create(displayName, phoneNumber, roleIds = setOf(roleId))
        if (isDisabled) {
            this.accountService.ban(account.id)
        } else {
            this.accountService.unban(account.id)
        }
        return account
    }

    @Transactional
    fun update(id: UUID, displayName: String, phoneNumber: String, roleId: UUID, isDisabled: Boolean): UserAccount {
        val account = this.userAccountRepository.findById(id).orElseThrow()
        account.displayName = displayName
        account.phoneNumber = phoneNumber
        account.roleIds = setOf(roleId)
        if (isDisabled) {
            this.accountService.ban(id)
        } else {
            this.accountService.unban(id)
        }
        return this.userAccountRepository.save(account)
    }
}