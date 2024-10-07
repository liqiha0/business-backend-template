package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.Identity
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.repository.TokenRepository
import io.github.liqiha0.template.core.domain.specification.TokenSpecification.principalIdEqual
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PrincipalApplicationService(
    private val principalRepository: PrincipalRepository,
    private val tokenRepository: TokenRepository
) {

    @Transactional
    fun deletePrincipal(principalId: UUID) {
        val tokens = tokenRepository.findAll(principalIdEqual(principalId))
        tokenRepository.deleteAll(tokens)
        principalRepository.deleteById(principalId)
    }

    @Transactional
    fun addIdentities(principalId: UUID, identities: Collection<Identity>) = run {
        val account = principalRepository.findById(principalId).orElseThrow()

        identities.forEach { idt ->
            if (!account.identities.contains(idt)) {
                account.addIdentity(idt)
            }
        }

        try {
            principalRepository.save(account)
        } catch (e: DataIntegrityViolationException) {
            throw IllegalArgumentException("identity already bound by another account", e)
        }
    }

    @Transactional
    fun addIdentity(principalId: UUID, identity: Identity) = addIdentities(principalId, listOf(identity))
}
