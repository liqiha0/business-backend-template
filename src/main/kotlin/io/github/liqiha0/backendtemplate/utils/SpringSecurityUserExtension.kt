package io.github.liqiha0.backendtemplate.utils

import org.springframework.security.core.userdetails.User
import java.util.*

val User.userId: UUID get() = UUID.fromString(this.username)