package io.github.liqiha0.template.core.application

import io.github.liqiha0.template.core.domain.model.iam.Administrator
import io.github.liqiha0.template.core.domain.model.iam.RoleRepository
import io.github.liqiha0.template.core.domain.model.iam.displayNameEqual
import io.github.liqiha0.template.core.domain.service.RbacService
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
