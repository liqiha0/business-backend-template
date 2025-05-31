package io.github.liqiha0.backendtemplate.domain.model.system.order

import io.github.liqiha0.backendtemplate.domain.shared.AuditableAggregateRoot
import jakarta.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.math.BigDecimal
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Order<T : Order<T>>(val userId: UUID, val amount: BigDecimal) : AuditableAggregateRoot<T>() {
    init {
        if (this.amount <= BigDecimal.ZERO) {
            this.paymentComplete()
        }
    }

    @Id
    val id: String = UUID.randomUUID().toString().replace("-", "")

    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus = PaymentStatus.PENDING
        internal set(value) {
            check(field == PaymentStatus.PENDING)
            field = value
        }

    @Enumerated(EnumType.STRING)
    var paymentChannel: PaymentChannel? = null
        internal set

    fun paymentComplete(channel: PaymentChannel? = null) {
        this.paymentChannel = channel
        this.paymentStatus = PaymentStatus.COMPLETED
        this.registerEvent(OrderCompletedEvent(this.id))
    }

    fun cancel() {
        this.paymentStatus = PaymentStatus.CANCELLED
        this.registerEvent(OrderCancelledEvent(this.id))
    }
}

enum class PaymentStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

enum class PaymentChannel {
    ALIPAY,
    WECHAT_PAY
}

class OrderCreatedEvent(val orderId: String)
class OrderCancelledEvent(val orderId: String)
class OrderCompletedEvent(val orderId: String)

interface OrderRepository : JpaRepository<Order<*>, String>, JpaSpecificationExecutor<Order<*>>

fun idEqual(id: String): Specification<Order<*>> {
    return Specification<Order<*>> { root, query, criteriaBuilder -> criteriaBuilder.equal(root.get<String>("id"), id) }
}

fun userIdEqual(userId: UUID): Specification<Order<*>> {
    return Specification<Order<*>> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<UUID>("userId"), userId)
    }
}
