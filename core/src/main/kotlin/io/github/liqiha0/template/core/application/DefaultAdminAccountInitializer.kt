package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.AccountFactory
import io.github.liqiha0.template.core.domain.model.iam.AccountRepository
import io.github.liqiha0.template.core.domain.model.iam.AccountSpecifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// TODO: 重构
const val DEFAULT_ADMIN_USERNAME = "admin"
const val DEFAULT_ADMIN_PASSWORD = "admin123"

@Service
class DefaultAdminAccountInitializer(
    val accountFactory: AccountFactory,
    val accountRepository: AccountRepository
) {

    @Transactional
    fun createDefaultAdminAccount() {
        if (!this.accountRepository.exists(AccountSpecifications.hasUsername(DEFAULT_ADMIN_USERNAME))) {
            val account = this.accountFactory.createWithUsernamePassword(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD)
            this.accountRepository.save(account)
        }
    }
}
