package io.github.liqiha0.backendtemplate.infrastructure

import io.github.liqiha0.backendtemplate.application.system.AuthorityProvider
import io.github.liqiha0.backendtemplate.application.system.AuthorityRegistry
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AuthorityInitializer(
    private val authorityRegistry: AuthorityRegistry,
    private val providers: List<AuthorityProvider>,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        providers.forEach { it.registerAuthorities(this.authorityRegistry) }
    }
}