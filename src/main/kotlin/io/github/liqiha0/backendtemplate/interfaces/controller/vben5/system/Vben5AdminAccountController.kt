package io.github.liqiha0.backendtemplate.interfaces.controller.vben5.system

import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccount
import io.github.liqiha0.backendtemplate.domain.model.system.AdminAccountRepository
import io.github.liqiha0.backendtemplate.domain.service.system.AdminAccountService
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenResult
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.vbenSuccess
import io.github.liqiha0.backendtemplate.interfaces.controller.vben5.Vben5Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/vben5/admin-account")
@Vben5Response
@Tag(name = "Vben5/管理员账户")
class Vben5AdminAccountController(
    private val repository: AdminAccountRepository,
    private val adminAccountService: AdminAccountService,
) {
    @GetMapping
    fun page(pageable: Pageable): Page<AdminAccount> {
        return this.repository.findAll(pageable)
    }

    data class CreateAdminAccountParam(
        val displayName: String,
        val username: String,
        val password: String,
        val roleIds: Set<UUID>,
    )

    @PostMapping
    @Operation(summary = "创建账户")
    fun create(@RequestBody body: CreateAdminAccountParam): VbenResult<AdminAccount> {
        val account = this.adminAccountService.create(body.displayName, body.username, body.password, body.roleIds)
        return vbenSuccess(account)
    }

    data class ChangeAdminAccountPasswordParam(val password: String)

    @PatchMapping("/{id}/password")
    @Operation(summary = "修改密码")
    fun changePassword(
        @PathVariable id: UUID,
        @RequestBody body: ChangeAdminAccountPasswordParam,
    ): VbenResult<AdminAccount> {
        this.adminAccountService.changePassword(id, body.password)
        return vbenSuccess(this.repository.findByIdOrNull(id))
    }
}