package io.github.liqiha0.template.core.interfaces.controller

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Vben5Response
abstract class Vben5CrudController<T : AuditableAggregateRoot<T>, ID : Any>(
    private val crudRepository: CrudRepository<T, ID>,
    private val updater: ((from: T, to: T) -> Unit)? = null
) {
    protected open val crudController by lazy { AggregateRootController(this.crudRepository, this.updater) }

    @GetMapping("/{id}")
    @Operation(summary = "查询单个实体")
    fun getById(@PathVariable("id") id: ID): Vben5Result<T> {
        return vben5Success(this.crudController.getById(id))
    }

    @PostMapping("/batch")
    @Operation(summary = "查询多个实体")
    fun batch(@RequestBody body: List<ID>): Vben5Result<Iterable<T>> {
        return vben5Success(this.crudController.batch(body))
    }

    @PostMapping
    @Transactional
    @Operation(summary = "新增实体")
    open fun create(@RequestBody entity: T): Vben5Result<T> {
        return vben5Success(this.crudController.create(entity))
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "更新实体")
    open fun update(@PathVariable id: ID, @RequestBody entity: T): Vben5Result<T> {
        return vben5Success(this.crudController.update(id, entity))
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "删除实体")
    open fun delete(@PathVariable id: ID): Vben5Result<Unit> {
        this.crudController.delete(id)
        return vben5Success()
    }
}