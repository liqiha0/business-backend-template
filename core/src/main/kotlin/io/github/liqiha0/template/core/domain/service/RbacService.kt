package io.github.liqiha0.template.core.domain.service

import io.github.liqiha0.template.core.domain.model.iam.*
import io.github.liqiha0.template.core.domain.repository.PrincipalRepository
import io.github.liqiha0.template.core.domain.repository.RoleRepository
import io.github.liqiha0.template.core.domain.shared.BusinessException
import io.github.liqiha0.template.core.infrastructure.config.SuperuserProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RbacService(
    private val principalRepository: PrincipalRepository,
    private val roleRepository: RoleRepository,
    private val authorityRegistry: AuthorityRegistry,
    private val superuser: SuperuserProperties,
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
        if (superuser.enabled && id == superuser.principalId) {
            return getAuthoritiesOfSuperuser()
        }

        val account = this.principalRepository.findByIdOrNull(id) ?: throw NoSuchElementException()
        val usernameIdentity = account.getIdentity<UsernameIdentity>()

        val roles = this.roleRepository.findAllById(account.roleIds)
        return buildSet {
            roles.forEach { role ->
                role.authority.forEach { authorityKey ->
                    this@RbacService.authorityRegistry.getAuthority(authorityKey)?.let { authority ->
                        add(authority)
                        addAll(getChildAuthorities(authority))
                    }
                }
            }
        }
    }

    fun getAuthoritiesOfSuperuser(): Set<Authority> {
        return setOf(Administrator)
    }

    private fun getChildAuthorities(authority: Authority): Set<Authority> {
        return this.authorityRegistry.getAllAuthorities()
            .filter { it.parent == authority }
            .flatMap { child -> setOf(child) + getChildAuthorities(child) }
            .toSet()
    }
}