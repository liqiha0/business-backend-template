package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.PrincipalFactory
import io.github.liqiha0.template.core.domain.model.iam.PrincipalRepository
import io.github.liqiha0.template.core.domain.model.iam.PrincipalSpecifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// TODO: 重构
const val DEFAULT_ADMIN_USERNAME = "admin"
const val DEFAULT_ADMIN_PASSWORD = "admin123"

@Service
class DefaultAdminAccountInitializer(
    val principalFactory: PrincipalFactory,
    val principalRepository: PrincipalRepository
) {

    @Transactional
    fun createDefaultAdminAccount() {
        if (!this.principalRepository.exists(PrincipalSpecifications.hasUsername(DEFAULT_ADMIN_USERNAME))) {
            val account =
                this.principalFactory.createWithUsernamePassword(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD)
            this.principalRepository.save(account)
        }
    }
}
