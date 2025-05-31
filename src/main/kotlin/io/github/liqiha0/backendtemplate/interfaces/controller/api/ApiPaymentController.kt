package io.github.liqiha0.backendtemplate.interfaces.controller.api

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum
import com.github.binarywang.wxpay.service.WxPayService
import io.github.liqiha0.backendtemplate.domain.model.system.UserAccountRepository
import io.github.liqiha0.backendtemplate.domain.model.system.order.OrderRepository
import io.github.liqiha0.backendtemplate.domain.model.system.order.PaymentStatus
import io.github.liqiha0.backendtemplate.domain.model.system.order.idEqual
import io.github.liqiha0.backendtemplate.domain.model.system.order.userIdEqual
import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import io.github.liqiha0.backendtemplate.utils.userId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ConditionalOnBean(WxPayService::class)
@RestController
@RequestMapping("/api/payment-wechat-pay")
@Tag(name = "用户端/微信支付")
class ApiPaymentController(
    private val wxPayService: WxPayService,
    private val userAccountRepository: UserAccountRepository,
    private val orderRepository: OrderRepository,
    @Value("\${payment.wechat.notify-url}") val wechatNotifyUrl: String
) {
    data class WechatPrepayParam(val orderId: String)

    @PostMapping("/prepay")
    @Operation(summary = "微信支付")
    @Transactional
    fun wechat(
        @AuthenticationPrincipal user: User,
        @RequestBody body: WechatPrepayParam
    ): WxPayUnifiedOrderV3Result.JsapiResult {
        val order = this.orderRepository.findOne(idEqual(body.orderId).and(userIdEqual(user.userId))).orElseThrow()
        if (order.paymentStatus != PaymentStatus.PENDING) throw BusinessException("订单状态异常")

        val account = this.userAccountRepository.findByIdOrNull(user.userId) ?: throw NoSuchElementException()
        val request = WxPayUnifiedOrderV3Request()
        request.description = "订单支付"
        request.outTradeNo = order.id
        request.notifyUrl = this.wechatNotifyUrl
        val amount = WxPayUnifiedOrderV3Request.Amount()
        amount.total = BaseWxPayRequest.yuan2Fen(order.amount)
        request.amount = amount
        val payer = WxPayUnifiedOrderV3Request.Payer()
        val openId = account.wechatBinding?.openId ?: throw BusinessException("未绑定微信")
        payer.openid = openId
        request.payer = payer

        val result: WxPayUnifiedOrderV3Result.JsapiResult =
            this.wxPayService.createOrderV3(TradeTypeEnum.JSAPI, request)
        return result
    }

}