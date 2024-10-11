package io.github.liqiha0.backendtemplate.interfaces.controller.vben.system

import io.github.liqiha0.backendtemplate.domain.service.system.RbacService
import io.github.liqiha0.backendtemplate.domain.service.system.TokenManager
import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import io.github.liqiha0.backendtemplate.utils.userId
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenResponse
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenResult
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.vbenError
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.vbenSuccess
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vben")
@VbenResponse
@Tag(name = "Vben/系统API")
class VbenSystemController(
    private val authenticationManager: AuthenticationManager,
    private val accountRepository: AdminAccountRepository,
    private val tokenManager: TokenManager,
    private val rbacService: RbacService,
) {

    data class LoginBody(val username: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody body: LoginBody): VbenResult<out Any> {
        val (username, password) = body
        val authenticate = this.authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(username, password))
        if (authenticate.isAuthenticated) {
            val account =
                this.accountRepository.findByIdOrNull((authenticate.principal as User).userId)
                    ?: throw NoSuchElementException()
            val token = this.tokenManager.createToken(account.id)
            return vbenSuccess(
                mapOf(
                    "roles" to this.rbacService.getAuthoritiesOfAccount(account.id).map(Authority::key),
                    "userId" to account.id.toString(),
                    "username" to account.username,
                    "token" to token.accessToken,
                    "realName" to account.displayName,
                    "desc" to account.displayName
                )
            )
        } else {
            return vbenError("用户名或密码错误")
        }
    }

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取用户信息")
    fun getUserInfo(@AuthenticationPrincipal principal: User): Map<String, Any> {
        val account = this.accountRepository.findByIdOrNull(principal.userId) ?: throw NoSuchElementException()
        return mapOf(
            "userId" to account.id.toString(),
            "username" to account.username,
            "realName" to account.displayName,
            "desc" to account.displayName,
            "roles" to this.rbacService.getAuthoritiesOfAccount(account.id).map(Authority::key)
        )
    }

}