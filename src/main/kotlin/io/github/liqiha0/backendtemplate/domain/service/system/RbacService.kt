package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.*
import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RbacService(
    private val accountRepository: AccountRepository<Account<*>>,
    private val roleRepository: RoleRepository,
    private val authorityRegistry: AuthorityRegistry,
) {

    @Transactional
    fun createRole(displayName: String, authorities: Set<String>): Role {
        val role = Role(displayName, authorities)
        return this.roleRepository.save(role)
    }

    @Transactional
    fun createBuiltinRole(displayName: String, authorities: Set<String>): Role {
        val role = Role(displayName, authorities, true)
        return this.roleRepository.save(role)
    }

    @Transactional
    fun deleteRole(id: UUID) {
        val role = this.roleRepository.findById(id).orElseThrow()
        if (role.isBuiltin) throw BusinessException("内置角色不可删除")
        this.roleRepository.delete(role)
    }

    @Transactional
    fun updateRole(id: UUID, displayName: String, authorities: Set<String>) {
        val role = this.roleRepository.findById(id).orElseThrow()
        if (role.isBuiltin) throw BusinessException("内置角色不可更新")
        role.displayName = displayName
        role.authority = authorities
        this.roleRepository.save(role)
    }

    fun getAuthoritiesOfAccount(id: UUID): Set<Authority> {
        val account = this.accountRepository.findByIdOrNull(id) ?: throw NoSuchElementException()
        if (account is AdminAccount && account.username == DEFAULT_ADMIN_USERNAME) {
            return setOf(Administrator)
        }
        val roles = this.roleRepository.findAllById(account.roleIds)
        return buildSet {
            roles.forEach { role ->
                role.authority.forEach { authorityKey ->
                    authorityRegistry.getAuthority(authorityKey)?.let { authority ->
                        add(authority)
                        addAll(getChildAuthorities(authority))
                    }
                }
            }
        }
    }

    private fun getChildAuthorities(authority: Authority): Set<Authority> {
        return authorityRegistry.getAllAuthorities()
            .filter { it.parent == authority }
            .flatMap { child -> setOf(child) + getChildAuthorities(child) }
            .toSet()
    }
}