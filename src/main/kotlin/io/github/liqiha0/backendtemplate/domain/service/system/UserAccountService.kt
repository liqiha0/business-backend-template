package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.UserAccount
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL
import java.util.*

@Service
class UserAccountService(
    private val userAccountRepository: UserAccountRepository
) {
    @Transactional
    fun create(
        displayName: String,
        phoneNumber: String? = null,
        avatar: URL? = null,
        roleIds: Set<UUID> = emptySet()
    ): UserAccount {
        return this.userAccountRepository.save(UserAccount(displayName, phoneNumber, avatar, roleIds))
    }
}