package io.github.liqiha0.backendtemplate.interfaces.controller.vben5.system

import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import io.github.liqiha0.backendtemplate.domain.service.system.RbacService
import io.github.liqiha0.backendtemplate.domain.service.system.TokenManager
import io.github.liqiha0.backendtemplate.interfaces.controller.vben5.Vben5Response
import io.github.liqiha0.backendtemplate.interfaces.controller.vben5.Vben5Result
import io.github.liqiha0.backendtemplate.interfaces.controller.vben5.vben5Error
import io.github.liqiha0.backendtemplate.interfaces.controller.vben5.vben5Success
import io.github.liqiha0.backendtemplate.utils.userId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vben5")
@Vben5Response
@Tag(name = "Vben5/系统API")
class Vben5SystemController(
    private val authenticationManager: AuthenticationManager,
    private val accountRepository: AdminAccountRepository,
    private val tokenManager: TokenManager,
    private val rbacService: RbacService,
) {

    data class LoginBody(val username: String, val password: String)

    @PostMapping("/auth/login")
    fun login(@RequestBody body: LoginBody): Vben5Result<out Any> {
        val (username, password) = body
        val authenticate = this.authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(username, password))
        if (authenticate.isAuthenticated) {
            val account =
                this.accountRepository.findByIdOrNull((authenticate.principal as User).userId)
                    ?: throw NoSuchElementException()
            val token = this.tokenManager.createToken(account.id)
            return vben5Success(
                mapOf(
                    "roles" to this.rbacService.getAuthoritiesOfAccount(account.id).map(Authority::key),
                    "id" to account.id.toString(),
                    "username" to account.username,
                    "accessToken" to token.accessToken,
                    "realName" to account.displayName
                )
            )
        } else {
            return vben5Error("用户名或密码错误")
        }
    }

    @GetMapping("/auth/codes")
    fun codes(@AuthenticationPrincipal user: User): Vben5Result<List<String>> {
        return vben5Success(emptyList())
    }

    @GetMapping("/user/info")
    @Operation(summary = "获取用户信息")
    fun getUserInfo(@AuthenticationPrincipal principal: User): Map<String, Any> {
        val account = this.accountRepository.findByIdOrNull(principal.userId) ?: throw NoSuchElementException()
        return mapOf(
            "id" to account.id.toString(),
            "username" to account.username,
            "realName" to account.displayName,
            "roles" to this.rbacService.getAuthoritiesOfAccount(account.id).map(Authority::key)
        )
    }

}