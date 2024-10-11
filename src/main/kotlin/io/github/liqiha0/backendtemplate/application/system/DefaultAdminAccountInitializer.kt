package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.DEFAULT_ADMIN_USERNAME
import io.github.liqiha0.backendtemplate.domain.model.system.usernameEqual
import io.github.liqiha0.backendtemplate.domain.service.system.AdminAccountService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultAdminAccountInitializer(
    val adminAccountRepository: AdminAccountRepository,
    val accountService: AdminAccountService,
    val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun createDefaultAdminAccount() {
        if (!this.adminAccountRepository.exists(usernameEqual(DEFAULT_ADMIN_USERNAME))) {
            this.accountService.create(
                "超级管理员",
                DEFAULT_ADMIN_USERNAME,
                this.passwordEncoder.encode("admin123")
            )
        }
    }
}
