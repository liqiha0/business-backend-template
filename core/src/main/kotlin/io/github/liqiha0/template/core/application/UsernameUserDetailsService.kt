package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.*
import io.github.liqiha0.template.core.domain.service.RbacService
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
import kotlin.jvm.optionals.getOrNull

@Service
@Primary
class UsernameUserDetailsService(
    val accountRepository: AccountRepository,
    val rbacService: RbacService,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val account = this.accountRepository.findOne(AccountSpecifications.hasUsername(username)).getOrNull()
            ?: throw UsernameNotFoundException(username)
        val authorities = this.rbacService.getAuthoritiesOfAccount(account.id)
        val credential = account.getCredential<UsernamePasswordCredential>() ?: throw IllegalStateException()
        return User(
            account.id.toString(),
            credential.passwordHash,
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )
    }
}

@Service
class IdUserDetailsService(
    val accountRepository: AccountRepository,
    val rbacService: RbacService,
) : UserDetailsService {
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    override fun loadUserByUsername(username: String): UserDetails {
        val account = this.accountRepository.findByIdOrNull(UUID.fromString(username))
            ?: throw UsernameNotFoundException(username)
        val authorities = this.rbacService.getAuthoritiesOfAccount(account.id)
        return User(
            account.id.toString(),
            "N/A",
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )
    }
}