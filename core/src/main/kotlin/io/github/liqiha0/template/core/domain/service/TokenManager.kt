package io.github.liqiha0.template.core.domain.service

import io.github.liqiha0.template.core.domain.model.iam.Token
import io.github.liqiha0.template.core.domain.model.iam.TokenRepository
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