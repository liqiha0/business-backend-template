package io.github.liqiha0.template.core.domain.service

import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.repository.TokenRepository
import io.github.liqiha0.template.core.domain.specification.TokenSpecification.principalIdEqual
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PrincipalService(
    private val principalRepository: PrincipalRepository,
    private val tokenRepository: TokenRepository
) {
    @Transactional
    fun ban(userId: UUID) {
        val account = this.principalRepository.findById(userId).orElseThrow()
        account.disabled = true
        this.principalRepository.save(account)
        this.tokenRepository.delete(principalIdEqual(userId))
    }

    @Transactional
    fun unban(userId: UUID) {
        val account = this.principalRepository.findById(userId).orElseThrow()
        account.disabled = false
        this.principalRepository.save(account)
    }

    @Transactional
    fun delete(id: UUID) {
        this.principalRepository.deleteById(id)
        this.tokenRepository.delete(principalIdEqual(id))
    }
}