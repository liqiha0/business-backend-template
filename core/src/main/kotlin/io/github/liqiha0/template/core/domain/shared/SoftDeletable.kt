package io.github.liqiha0.template.core.domain.shared

/**
 * Marks an entity as soft-deletable.
 * Entities implementing this interface will not be permanently removed from the database.
 * Instead, the 'deleted' flag will be set to true.
 */
interface SoftDeletable {
    var deleted: Boolean
}
