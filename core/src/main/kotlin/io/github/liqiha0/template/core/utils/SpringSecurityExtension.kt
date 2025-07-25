package io.github.liqiha0.template.core.utils

import org.springframework.security.core.userdetails.UserDetails
import java.util.*

val UserDetails.accountId: UUID get() = UUID.fromString(this.username)