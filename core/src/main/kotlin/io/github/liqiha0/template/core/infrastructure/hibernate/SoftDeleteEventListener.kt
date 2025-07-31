package io.github.liqiha0.template.core.infrastructure.hibernate

import io.github.liqiha0.template.core.domain.shared.SoftDeletable
import org.hibernate.event.spi.PreDeleteEvent
import org.hibernate.event.spi.PreDeleteEventListener
import org.springframework.stereotype.Component

@Component
class SoftDeleteEventListener : PreDeleteEventListener {

    override fun onPreDelete(event: PreDeleteEvent): Boolean {
        val entity = event.entity
        if (entity !is SoftDeletable) {
            return false
        }

        entity.deleted = true

        val session = event.session
        session.merge(entity)

        return true
    }
}
