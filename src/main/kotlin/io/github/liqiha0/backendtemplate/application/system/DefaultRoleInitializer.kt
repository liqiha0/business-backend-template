package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.Administrator
import io.github.liqiha0.backendtemplate.domain.model.system.RoleRepository
import io.github.liqiha0.backendtemplate.domain.model.system.displayNameEqual
import io.github.liqiha0.backendtemplate.domain.service.system.RbacService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

const val ADMIN_ROLE_NAME = "系统管理员"

@Service
class DefaultRoleInitializer(
    val roleRepository: RoleRepository,
    val rbacService: RbacService
) {

    @Transactional
    fun createDefaultAdminRole() {
        if (!this.roleRepository.exists(displayNameEqual(ADMIN_ROLE_NAME))) {
            this.rbacService.createRole(ADMIN_ROLE_NAME, setOf(Administrator.key))
        }
    }
}
