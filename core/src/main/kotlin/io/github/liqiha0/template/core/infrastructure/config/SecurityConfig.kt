package io.github.liqiha0.template.core.infrastructure.config

import io.github.liqiha0.template.core.application.UsernameUserDetailsService
import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.service.AuthorityRegistry
import io.github.liqiha0.template.core.infrastructure.security.BearerTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    fun bearerTokenFilter(authenticationManager: AuthenticationManager): BearerTokenFilter {
        return BearerTokenFilter(authenticationManager)
    }

    @Bean
    fun passwordAuthenticationProvider(
        userDetailsService: UsernameUserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return authenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(
        authenticationProviders: List<AuthenticationProvider>
    ): AuthenticationManager {
        return ProviderManager(authenticationProviders)
    }

    @Bean
    fun roleHierarchy(authorityRegistry: AuthorityRegistry): RoleHierarchy {
        fun buildHierarchy(authority: Authority, hierarchy: StringBuilder, visited: MutableSet<Authority>) {
            if (visited.contains(authority)) return
            visited.add(authority)

            authority.parent?.let { parent ->
                hierarchy.append("${authority.key} > ${parent.key}\n")
                buildHierarchy(parent, hierarchy, visited)
            }
        }

        fun getHierarchy(): String {
            val hierarchy = StringBuilder()
            val visited = mutableSetOf<Authority>()

            authorityRegistry.getAllAuthorities().forEach { authority ->
                buildHierarchy(authority, hierarchy, visited)
            }

            return hierarchy.toString()
        }

        val roleHierarchy = RoleHierarchyImpl.fromHierarchy(getHierarchy())
        return roleHierarchy
    }
}
