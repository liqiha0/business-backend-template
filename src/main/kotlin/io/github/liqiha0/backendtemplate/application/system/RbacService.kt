package io.github.liqiha0.backendtemplate.application.system

import io.github.liqiha0.backendtemplate.domain.model.system.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class RbacService(
    private val adminAccountRepository: AdminAccountRepository,
    private val roleRepository: RoleRepository,
    private val authorityRegistry: AuthorityRegistry,
) {
    fun getAuthoritiesOfAccount(id: UUID): Set<Authority> {
        val account = this.adminAccountRepository.findByIdOrNull(id) ?: throw NoSuchElementException()
        if (account.username == DEFAULT_ADMIN_USERNAME) {
            return setOf(Administrator)
        }
        val roles = this.roleRepository.findAllById(account.roleIds)
        return buildSet {
            roles.forEach { role ->
                role.authority.forEach {
                    authorityRegistry.getAuthority(it)?.let(::add)
                }
            }
        }
    }
}