package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.PrincipalDisabledEvent
import io.github.liqiha0.template.core.domain.specification.TokenSpecification.principalIdEqual
import io.github.liqiha0.template.core.domain.repository.TokenRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PrincipalEventHandler(
    private val tokenRepository: TokenRepository
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handlePrincipalDisabled(event: PrincipalDisabledEvent) {
        tokenRepository.delete(principalIdEqual(event.principalId))
    }
}
