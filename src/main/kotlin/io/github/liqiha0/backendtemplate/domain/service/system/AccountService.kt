package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.AccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccount
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccount
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(
    val accountRepository: AccountRepository,
) {
    fun createAdmin(displayName: String, username: String, password: String): AdminAccount {
        return this.accountRepository.save(AdminAccount(displayName, username, password))
    }

    fun createUser(phoneNumber: String, displayName: String): UserAccount {
        return this.accountRepository.save(UserAccount(displayName, phoneNumber))
    }
}