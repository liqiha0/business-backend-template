package io.github.liqiha0.template.core.infrastructure.security

import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.repository.TokenRepository
import io.github.liqiha0.template.core.domain.service.RbacService
import io.github.liqiha0.template.core.domain.specification.TokenSpecification
import io.github.liqiha0.template.core.infrastructure.config.SuperuserProperties
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Component
class AccessTokenAuthenticationProvider(
    private val tokenRepository: TokenRepository,
    private val principalRepository: PrincipalRepository,
    private val rbacService: RbacService,
    private val superuser: SuperuserProperties,
) : AuthenticationProvider {
    @Transactional(readOnly = true)
    override fun authenticate(authentication: Authentication): Authentication? {
        val raw = (authentication as? AccessTokenAuthenticationToken)?.token
            ?: return authentication

        if (superuser.enabled && superuser.accessToken.isNotBlank() && raw == superuser.accessToken) {
            val authorities = rbacService.getAuthoritiesOfSuperuser()
            val user = User(
                superuser.principalId.toString(),
                "N/A",
                AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
            )
            val authenticated = AccessTokenAuthenticationToken(raw, user.authorities, user)
            authenticated.isAuthenticated = true
            return authenticated
        }

        val tokenEntity = tokenRepository.findOne(TokenSpecification.accessTokenEqual(raw)).getOrNull()
            ?: return null

        val principal = principalRepository.findById(tokenEntity.principalId).getOrNull()
            ?: return null

        if (principal.disabled) throw DisabledException("Account disabled")

        val authorities = rbacService.getAuthoritiesOfAccount(principal.id)
        val user = User(
            principal.id.toString(),
            "N/A",
            AuthorityUtils.createAuthorityList(authorities.map(Authority::key))
        )

        val authenticated = AccessTokenAuthenticationToken(raw, user.authorities, user)
        authenticated.isAuthenticated = true
        return authenticated
    }

    override fun supports(authentication: Class<*>): Boolean =
        AccessTokenAuthenticationToken::class.java.isAssignableFrom(authentication)
}
