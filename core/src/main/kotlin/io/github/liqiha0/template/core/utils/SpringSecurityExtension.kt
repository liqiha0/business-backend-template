package io.github.liqiha0.template.core.utils

import org.springframework.security.core.userdetails.UserDetails
import java.util.*

val UserDetails.principalId: UUID get() = UUID.fromString(this.username)