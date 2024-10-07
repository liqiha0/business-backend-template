package io.github.liqiha0.template.order.domain.model.order

import io.github.liqiha0.template.core.domain.shared.AuditableAggregateRoot
import jakarta.persistence.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.math.BigDecimal
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Order<T : Order<T>>(
    @Column(nullable = false)
    val userId: UUID,
    @Column(nullable = false)
    val amount: BigDecimal
) : AuditableAggregateRoot<T>() {
    init {
        require(amount > BigDecimal.ZERO)
    }

    @Id
    @Column(nullable = false)
    val id: String = UUID.randomUUID().toString().replace("-", "")

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var paymentStatus: PaymentStatus = PaymentStatus.PENDING
        protected set(value) {
            require(field == PaymentStatus.PENDING)
            field = value
        }

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    var paymentChannel: PaymentChannel? = null
        protected set

    open fun complete(channel: PaymentChannel? = null) {
        this.paymentChannel = channel
        this.paymentStatus = PaymentStatus.COMPLETED
        this.registerEvent(OrderCompletedEvent(this.id))
    }

    open fun cancel() {
        require(this.paymentStatus == PaymentStatus.PENDING)
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
    WECHAT_PAY,
    BALANCE
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
