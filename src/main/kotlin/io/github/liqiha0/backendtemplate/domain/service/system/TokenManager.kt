package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.Token
import io.github.liqiha0.backendtemplate.domain.model.system.TokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TokenManager(
    private val tokenRepository: TokenRepository,
) {
    @Transactional
    fun createToken(accountId: UUID): Token {
        return this.tokenRepository.save(Token(accountId, UUID.randomUUID().toString()))
    }
}