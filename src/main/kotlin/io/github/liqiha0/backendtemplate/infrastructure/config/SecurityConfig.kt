package io.github.liqiha0.backendtemplate.infrastructure.config

import io.github.liqiha0.backendtemplate.application.system.AdminUserDetailService
import io.github.liqiha0.backendtemplate.application.system.TokenUserDetailService
import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import io.github.liqiha0.backendtemplate.domain.model.system.AuthorityRegistry
import io.github.liqiha0.backendtemplate.domain.model.system.TokenRepository
import io.github.liqiha0.backendtemplate.infrastructure.security.TokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
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
        authenticationManager: AuthenticationManager,
        tokenAuthenticationFilter: TokenAuthenticationFilter
    ): SecurityFilterChain {
        http.authenticationManager(authenticationManager)
        http.csrf {
            it.disable()
        }
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.anonymous {
            it.disable()
        }
        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.GET, "/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

            it.requestMatchers(HttpMethod.GET, "/api/login/**")
            it.requestMatchers(HttpMethod.POST, "/api/upload")

            it.requestMatchers(HttpMethod.POST, "/vben/login", "/vben5/auth/login", "/vben5/upload").permitAll()

            it.anyRequest().authenticated()
        }

        http.exceptionHandling {
            it.authenticationEntryPoint { request, response, authException ->
                response.sendError(HttpStatus.UNAUTHORIZED.value())
            }
            it.accessDeniedHandler { request, response, accessDeniedException ->
                response.sendError(HttpStatus.FORBIDDEN.value())
            }
        }

        http.addFilter(tokenAuthenticationFilter)

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
    fun preAuthenticatedAuthenticationProvider(userDetailsService: TokenUserDetailService): PreAuthenticatedAuthenticationProvider {
        val provider = PreAuthenticatedAuthenticationProvider()
        val wrapper = UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(userDetailsService)
        provider.setPreAuthenticatedUserDetailsService(wrapper)
        return provider
    }

    @Bean
    fun adminAuthenticationProvider(
        userDetailsService: AdminUserDetailService,
        passwordEncoder: PasswordEncoder,
    ): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
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