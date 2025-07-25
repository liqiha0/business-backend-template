package io.github.liqiha0.template.core.infrastructure.config

import io.github.liqiha0.template.core.application.IdUserDetailsService
import io.github.liqiha0.template.core.application.UsernameUserDetailsService
import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.model.iam.TokenRepository
import io.github.liqiha0.template.core.domain.service.AuthorityRegistry
import io.github.liqiha0.template.core.infrastructure.ApiSecurityConfigurer
import io.github.liqiha0.template.core.infrastructure.security.TokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun filterChain(
        http: HttpSecurity,
        commonSecurityCustomizer: ApiSecurityConfigurer
    ): SecurityFilterChain {
        http.with(commonSecurityCustomizer, Customizer.withDefaults())
        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
            it.requestMatchers(HttpMethod.GET, "/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
        }
        return http.build()
    }

    @Bean
    fun tokenAuthenticationFilter(
        tokenRepository: TokenRepository,
        authenticationManager: AuthenticationManager,
    ): TokenAuthenticationFilter {
        val filter = TokenAuthenticationFilter(tokenRepository)
        filter.setAuthenticationManager(authenticationManager)
        return filter
    }

    @Bean
    fun preAuthenticatedAuthenticationProvider(userDetailsService: IdUserDetailsService): PreAuthenticatedAuthenticationProvider {
        val provider = PreAuthenticatedAuthenticationProvider()
        val wrapper = UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(userDetailsService)
        provider.setPreAuthenticatedUserDetailsService(wrapper)
        return provider
    }

    @Bean
    fun adminAuthenticationProvider(
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