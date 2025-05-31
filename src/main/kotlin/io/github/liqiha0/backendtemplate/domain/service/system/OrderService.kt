package io.github.liqiha0.backendtemplate.domain.service.system

import io.github.liqiha0.backendtemplate.domain.model.system.order.OrderRepository
import io.github.liqiha0.backendtemplate.domain.model.system.order.PaymentChannel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(private val orderRepository: OrderRepository) {
    @Transactional
    fun paymentComplete(id: String, paymentChannel: PaymentChannel) {
        val order = this.orderRepository.findById(id).orElseThrow()
        order.paymentComplete(paymentChannel)
        this.orderRepository.save(order)
    }
}