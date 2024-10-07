package io.github.liqiha0.template.core.interfaces.controller

import io.github.liqiha0.template.core.application.UsernamePasswordLoginService
import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.model.iam.UsernameIdentity
import io.github.liqiha0.template.core.domain.model.iam.getIdentity
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.service.RbacService
import io.github.liqiha0.template.core.utils.principalId
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Vben5Response
class Vben5SystemController(
    private val usernamePasswordLoginService: UsernamePasswordLoginService,
    private val principalRepository: PrincipalRepository,
    private val rbacService: RbacService,
) {
    data class LoginBody(val username: String, val password: String)

    @PostMapping("/auth/login")
    fun login(@RequestBody body: LoginBody): Vben5Result<out Any> {
        val (username, password) = body
        val token = this.usernamePasswordLoginService.login(username, password)
        return vben5Success(
            mapOf(
                "roles" to this.rbacService.getAuthoritiesOfAccount(token.principalId).map(Authority::key),
                "id" to token.principalId.toString(),
                "username" to username,
                "accessToken" to token.accessToken,
                "realName" to username
            )
        )
    }

    @GetMapping("/auth/codes")
    fun codes(@AuthenticationPrincipal user: User): Vben5Result<List<String>> {
        return vben5Success(emptyList())
    }

    @GetMapping("/user/info")
    @Operation(summary = "获取用户信息")
    fun getUserInfo(@AuthenticationPrincipal principal: User): Map<String, Any> {
        val account = this.principalRepository.findById(principal.principalId).orElseThrow()
        val credential = account.getIdentity<UsernameIdentity>()
        return mapOf(
            "id" to account.id.toString(),
            "username" to (credential?.username ?: "未知"),
            "realName" to (credential?.username ?: "未知"),
            "roles" to this.rbacService.getAuthoritiesOfAccount(account.id).map(Authority::key)
        )
    }
}

