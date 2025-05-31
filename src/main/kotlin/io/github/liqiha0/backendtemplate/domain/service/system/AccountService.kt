package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.Account
import io.github.liqiha0.backendtemplate.domain.model.system.AccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.TokenRepository
import io.github.liqiha0.backendtemplate.domain.model.system.userIdEqual
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository<Account<*>>,
    private val tokenRepository: TokenRepository
) {
    @Transactional
    fun ban(userId: UUID) {
        val account = this.accountRepository.findById(userId).orElseThrow()
        account.isDisabled = true
        this.accountRepository.save(account)
        this.tokenRepository.delete(userIdEqual(userId))
    }

    @Transactional
    fun unban(userId: UUID) {
        val account = this.accountRepository.findById(userId).orElseThrow()
        account.isDisabled = false
        this.accountRepository.save(account)
    }

    @Transactional
    fun delete(id: UUID) {
        this.accountRepository.deleteById(id)
        this.tokenRepository.delete(userIdEqual(id))
    }
}