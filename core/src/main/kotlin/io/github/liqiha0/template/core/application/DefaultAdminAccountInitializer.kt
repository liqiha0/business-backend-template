package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.Principal
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.specification.PrincipalSpecification
import io.github.liqiha0.template.core.domain.model.iam.PasswordCredential
import io.github.liqiha0.template.core.domain.model.iam.UsernameIdentity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// TODO: 重构
const val DEFAULT_ADMIN_USERNAME = "admin"
const val DEFAULT_ADMIN_PASSWORD = "admin123"

@Service
class DefaultAdminAccountInitializer(
    val principalRepository: PrincipalRepository,
    val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createDefaultAdminAccount() {
        if (!this.principalRepository.exists(PrincipalSpecification.hasUsername(DEFAULT_ADMIN_USERNAME))) {
            val account = Principal(
                identities = mutableSetOf(UsernameIdentity(DEFAULT_ADMIN_USERNAME)),
                credentials = mutableSetOf(PasswordCredential(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD)))
            )
            this.principalRepository.save(account)
        }
    }
}
