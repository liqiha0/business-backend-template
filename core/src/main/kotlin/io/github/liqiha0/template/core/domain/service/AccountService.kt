package io.github.liqiha0.template.core.domain.service

import io.github.liqiha0.template.core.domain.model.iam.AccountRepository
import io.github.liqiha0.template.core.domain.model.iam.TokenRepository
import io.github.liqiha0.template.core.domain.model.iam.userIdEqual
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val tokenRepository: TokenRepository
) {
    @Transactional
    fun ban(userId: UUID) {
        val account = this.accountRepository.findById(userId).orElseThrow()
        account.disabled = true
        this.accountRepository.save(account)
        this.tokenRepository.delete(userIdEqual(userId))
    }

    @Transactional
    fun unban(userId: UUID) {
        val account = this.accountRepository.findById(userId).orElseThrow()
        account.disabled = false
        this.accountRepository.save(account)
    }

    @Transactional
    fun delete(id: UUID) {
        this.accountRepository.deleteById(id)
        this.tokenRepository.delete(userIdEqual(id))
    }
}