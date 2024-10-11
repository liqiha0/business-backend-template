package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.AccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccount
import io.github.liqiha0.backendtemplate.domain.service.system.RbacService
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Primary
class AdminUserDetailService(
    val accountRepository: AdminAccountRepository,
    val rbacService: RbacService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val account = this.accountRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        val authorities = this.rbacService.getAuthoritiesOfAccount(account.id)
        return User(
            account.id.toString(),
            account.password,
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )
    }
}

@Service
class TokenUserDetailService(
    val accountRepository: AccountRepository,
    val rbacService: RbacService,
) : UserDetailsService {
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    override fun loadUserByUsername(username: String): UserDetails {
        val account = this.accountRepository.findByIdOrNull(UUID.fromString(username)) ?: throw UsernameNotFoundException(username)
        if (account is UserAccount) {
            return User(account.id.toString(), "N/A", AuthorityUtils.NO_AUTHORITIES)
        }
        val authorities = this.rbacService.getAuthoritiesOfAccount(account.id)
        return User(
            account.id.toString(),
            "N/A",
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )
    }
}