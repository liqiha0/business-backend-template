package io.github.liqiha0.template.core.domain.spi

import io.github.liqiha0.template.core.domain.model.iam.Authority

interface AuthorityProvider {
    val authorities: List<Authority>
}