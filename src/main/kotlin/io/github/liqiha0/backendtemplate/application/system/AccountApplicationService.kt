package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.AccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.TokenRepository
import io.github.liqiha0.backendtemplate.domain.model.system.userIdEqual
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountApplicationService(
    private val accountRepository: AccountRepository,
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
}