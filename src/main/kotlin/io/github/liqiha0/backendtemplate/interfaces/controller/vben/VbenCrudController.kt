package io.github.liqiha0.backendtemplate.interfaces.controller.vben

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@VbenResponse
open class VbenCrudController<T : AuditableAggregateRoot<T>, ID : Any>(
    private val crudRepository: JpaRepository<T, ID>,
) {

    @GetMapping("/{id}")
    @Operation(summary = "查询单个实体")
    open fun getById(@PathVariable("id") id: ID): VbenResult<T> {
        val entity = this.crudRepository.findByIdOrNull(id) ?: return vbenError("资源不存在", 404)
        return vbenSuccess(entity)
    }

    @PostMapping("/batch")
    @Operation(summary = "查询多个实体")
    fun batch(@RequestBody body: List<ID>): VbenResult<Iterable<T>> {
        return vbenSuccess(this.crudRepository.findAllById(body))
    }

    @PostMapping
    @Transactional
    @Operation(summary = "新增实体")
    open fun create(@RequestBody entity: T): VbenResult<T> {
        return vbenSuccess(this.crudRepository.save(entity))
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "更新实体")
    open fun update(@PathVariable id: ID, @RequestBody entity: T): VbenResult<T> {
        val existing = crudRepository.findByIdOrNull(id) ?: return vbenError("资源不存在")
        val kClass = existing::class

        for (property in kClass.declaredMemberProperties) {
            if (property.visibility == KVisibility.PUBLIC && property is KMutableProperty1<*, *> && property.setter.visibility == KVisibility.PUBLIC) {
                property as KMutableProperty1<Any, Any>
                property.isAccessible = true

                val newValue = property.get(entity)

                if (newValue is Collection<*>) {
                    val existingCollection = property.get(existing) as? MutableCollection<Any> ?: continue
                    existingCollection.clear()
                    existingCollection.addAll(newValue as Collection<Any>)
                } else {
                    property.set(existing, newValue)
                }            }
        }

        return vbenSuccess(this.crudRepository.save(existing))
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "删除实体")
    open fun delete(@PathVariable id: ID): VbenResult<Unit> {
        this.crudRepository.deleteById(id)
        return vbenSuccess()
    }
}