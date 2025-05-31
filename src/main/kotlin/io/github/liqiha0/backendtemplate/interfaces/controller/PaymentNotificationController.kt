package io.github.liqiha0.backendtemplate.interfaces.controller

import com.github.binarywang.wxpay.bean.notify.SignatureHeader
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response
import com.github.binarywang.wxpay.service.WxPayService
import io.github.liqiha0.backendtemplate.domain.model.system.order.PaymentChannel
import io.github.liqiha0.backendtemplate.domain.service.system.OrderService
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@ConditionalOnBean(WxPayService::class)
@RestController
@RequestMapping("/payment-notification-wechat-pay")
class WechatPayPaymentNotificationController(
    private val wxPayService: WxPayService,
    private val orderService: OrderService
) {
    @Transactional
    @PostMapping
    fun wechatPayNotification(
        @RequestBody body: String,
        @RequestHeader("Wechatpay-Timestamp") timestamp: String,
        @RequestHeader("Wechatpay-Nonce") nonce: String,
        @RequestHeader("Wechatpay-Serial") serial: String,
        @RequestHeader("Wechatpay-Signature") signature: String
    ): String {
        try {
            val signatureHeader = SignatureHeader(timestamp, nonce, signature, serial)
            val result = this.wxPayService.parseOrderNotifyV3Result(body, signatureHeader)
            this.orderService.paymentComplete(result.result.outTradeNo, PaymentChannel.WECHAT_PAY)
            return WxPayNotifyV3Response.success("成功")
        } catch (e: Exception) {
            return WxPayNotifyV3Response.fail(e.message)
        }
    }
}