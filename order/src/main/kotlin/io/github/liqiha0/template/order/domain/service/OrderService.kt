package io.github.liqiha0.template.order.domain.service

import io.github.liqiha0.template.order.domain.model.order.OrderRepository
import io.github.liqiha0.template.order.domain.model.order.PaymentChannel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(private val orderRepository: OrderRepository) {
    @Transactional
    fun paymentComplete(id: String, paymentChannel: PaymentChannel) {
        val order = this.orderRepository.findById(id).orElseThrow()
        order.complete(paymentChannel)
        this.orderRepository.save(order)
    }
}