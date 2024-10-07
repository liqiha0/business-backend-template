package io.github.liqiha0.template.core.application

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
    fun deletePrincipal(principalId: UUID) {
        val tokens = tokenRepository.findAll(principalIdEqual(principalId))
        tokenRepository.deleteAll(tokens)
        principalRepository.deleteById(principalId)
    }
}
