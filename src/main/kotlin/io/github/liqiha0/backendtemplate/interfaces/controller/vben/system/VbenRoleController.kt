package io.github.liqiha0.backendtemplate.interfaces.controller.vben.system

import io.github.liqiha0.backendtemplate.domain.model.system.AuthorityRegistry
import io.github.liqiha0.backendtemplate.domain.model.system.Authority
import io.github.liqiha0.backendtemplate.domain.model.system.Role
import io.github.liqiha0.backendtemplate.domain.model.system.RoleRepository
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenCrudController
import io.github.liqiha0.backendtemplate.interfaces.controller.vben.VbenResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/vben/roles")
@VbenResponse
@Tag(name = "Vben/角色")
class VbenRoleController(
    private val repository: RoleRepository,
    private val authorityRegistry: AuthorityRegistry,
) : VbenCrudController<Role, UUID>(repository) {
    @GetMapping
    fun page(pageable: Pageable): Page<Role> {
        return this.repository.findAll(pageable)
    }

    @GetMapping("/authority")
    fun getAllAuthorities(): Collection<Authority> {
        return this.authorityRegistry.getAllAuthorities()
    }
}