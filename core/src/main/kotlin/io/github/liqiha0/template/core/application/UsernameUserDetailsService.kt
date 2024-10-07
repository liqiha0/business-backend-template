package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.model.iam.PasswordCredential
import io.github.liqiha0.template.core.domain.model.iam.getCredential
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.service.RbacService
import io.github.liqiha0.template.core.domain.specification.PrincipalSpecification
import io.github.liqiha0.template.core.infrastructure.config.SuperuserProperties
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UsernameUserDetailsService(
    val principalRepository: PrincipalRepository,
    val rbacService: RbacService,
    val superuser: SuperuserProperties,
    val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        if (superuser.enabled && username == superuser.username) {
            val authorities = this.rbacService.getAuthoritiesOfSuperuser()
            return User(
                superuser.principalId.toString(),
                passwordEncoder.encode(superuser.password),
                AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
            )
        }

        val principal = this.principalRepository.findOne(
            PrincipalSpecification.hasUsername(username).or(
                PrincipalSpecification.hasPhone(username)
            )
        ).getOrNull()
            ?: throw UsernameNotFoundException(username)

        val passwordCredential = principal.getCredential<PasswordCredential>()
            ?: throw BadCredentialsException("User does not have a password set up.")

        val authorities = this.rbacService.getAuthoritiesOfAccount(principal.id)

        return User(
            principal.id.toString(),
            passwordCredential.passwordHash,
            !principal.disabled,
            true, true, true,
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )
    }
}
