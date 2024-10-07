package io.github.liqiha0.template.core.interfaces.controller

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.github.liqiha0.template.core.domain.shared.BusinessException
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

open class AggregateRootController<T : AuditableAggregateRoot<T>, ID : Any>(
    private val crudRepository: CrudRepository<T, ID>,
    private val updater: ((from: T, to: T) -> Unit)?
) {
    open fun getById(id: ID): T {
        val entity = this.crudRepository.findByIdOrNull(id) ?: throw BusinessException("资源不存在")
        return entity
    }

    open fun batch(body: List<ID>): Iterable<T> {
        return this.crudRepository.findAllById(body)
    }

    open fun create(entity: T): T {
        return this.crudRepository.save(entity)
    }

    open fun update(id: ID, entity: T): T {
        val existing = crudRepository.findByIdOrNull(id) ?: throw BusinessException("资源不存在")
        this.updater?.invoke(entity, existing) ?: throw NotImplementedError()
        return this.crudRepository.save(existing)
    }

    open fun delete(id: ID) {
        this.crudRepository.deleteById(id)
    }
}