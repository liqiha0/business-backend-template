package io.github.liqiha0.template.core.infrastructure

import io.github.liqiha0.template.core.domain.model.iam.Administrator
import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.spi.AuthorityProvider
import org.springframework.stereotype.Component

@Component
class AdminAuthorityProvider : AuthorityProvider {
    override val authorities: List<Authority> = listOf(Administrator)
}