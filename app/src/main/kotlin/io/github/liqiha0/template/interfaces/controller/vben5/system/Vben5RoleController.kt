package io.github.liqiha0.template.interfaces.controller.vben5.system

import io.github.liqiha0.template.core.domain.model.iam.Authority
import io.github.liqiha0.template.core.domain.model.iam.Role
import io.github.liqiha0.template.core.domain.model.iam.RoleRepository
import io.github.liqiha0.template.core.domain.service.AuthorityRegistry
import io.github.liqiha0.template.core.interfaces.controller.Vben5CrudController
import io.github.liqiha0.template.core.interfaces.controller.Vben5Response
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

private val updater = { from: Role, to: Role ->
    to.displayName = from.displayName
    to.authority = from.authority
}

@RestController
@RequestMapping("/vben5/roles")
@Vben5Response
@Tag(name = "Vben5/角色")
class Vben5RoleController(
    private val repository: RoleRepository,
    private val authorityRegistry: AuthorityRegistry,
) : Vben5CrudController<Role, UUID>(repository, updater = updater) {

    @GetMapping
    fun page(pageable: Pageable): Page<Role> {
        return this.repository.findAll(pageable)
    }

    @GetMapping("/authority")
    fun getAllAuthorities(): Collection<Authority> {
        return this.authorityRegistry.getAllAuthorities()
    }
}