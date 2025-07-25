package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.AccountRepository
import io.github.liqiha0.template.core.domain.model.iam.Token
import io.github.liqiha0.template.core.domain.service.TokenManager
import io.github.liqiha0.template.core.domain.shared.BusinessException
import io.github.liqiha0.template.core.utils.accountId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UsernamePasswordLoginService(
    private val authenticationManager: AuthenticationManager,
    private val accountRepository: AccountRepository,
    private val tokenManager: TokenManager
) {
    fun login(username: String, password: String): Token {
        val authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    username,
                    password
                )
            )
        } catch (_: BadCredentialsException) {
            throw BusinessException("用户名或密码错误")
        }

        val userDetails = authentication.principal as UserDetails
        val account = accountRepository.findByIdOrNull(userDetails.accountId)
            ?: throw BusinessException("用户不存在")

        return tokenManager.createToken(account.id)
    }
}
