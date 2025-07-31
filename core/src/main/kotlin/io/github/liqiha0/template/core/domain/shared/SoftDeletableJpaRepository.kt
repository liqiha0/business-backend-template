package io.github.liqiha0.template.core.domain.shared

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

/**
 * A base repository for entities that support soft deletion.
 * It ensures that list-based queries (findAll) automatically filter for non-deleted records,
 * while single-record queries (findById) can still retrieve soft-deleted records.
 */
@NoRepositoryBean
interface SoftDeletableJpaRepository<T, ID : Any> : JpaRepository<T, ID>, JpaSpecificationExecutor<T> where T : SoftDeletable {

    // --- 覆盖 JpaRepository 的 findAll 方法 ---
    // Spring Data JPA 会根据方法名自动生成查询 `... where deleted = false`

    @Query("select e from #{#entityName} e where e.deleted = false")
    override fun findAll(sort: Sort): List<T>

    @Query("select e from #{#entityName} e where e.deleted = false")
    override fun findAll(): List<T>

    @Query("select e from #{#entityName} e where e.deleted = false")
    override fun findAll(pageable: Pageable): Page<T>


    // --- 提供查询已删除数据的特定方法 ---

    @Query("select e from #{#entityName} e where e.id = :id and e.deleted = true")
    fun findDeletedById(id: ID): Optional<T>

    @Query("select e from #{#entityName} e where e.deleted = true")
    fun findAllDeleted(pageable: Pageable): Page<T>


    // --- 提供恢复删除的方法 ---

    @Modifying
    @Query("update #{#entityName} e set e.deleted = false where e.id = :id")
    fun undeleteById(id: ID)
}
