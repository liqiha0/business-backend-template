package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccount
import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminAccountService(
    private val adminAccountRepository: AdminAccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun create(displayName: String, username: String, password: String, roleIds: Set<UUID> = emptySet()): AdminAccount {
        return this.adminAccountRepository.save(
            AdminAccount(displayName, username, this.passwordEncoder.encode(password), roleIds)
        )
    }

    @Transactional
    fun changePassword(id: UUID, password: String, oldPassword: String? = null) {
        val account = this.adminAccountRepository.findById(id).orElseThrow()
        if (oldPassword != null) {
            if (!this.passwordEncoder.matches(oldPassword, account.password)) throw BusinessException("原密码不正确")
        }
        account.password = passwordEncoder.encode(password)
        this.adminAccountRepository.save(account)
    }
}