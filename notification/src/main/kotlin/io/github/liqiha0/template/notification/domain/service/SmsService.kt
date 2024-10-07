package io.github.liqiha0.template.notification.domain.service

data class SmsCalculationResult(
    val content: String,
    val count: Int
)

/**
 * 短信服务接口
 */
interface SmsService {
    /**
     * 发送短信
     * @param phoneNumbers 手机号列表
     * @param templateId 模板ID
     * @param templateParams 模板参数列表，按顺序替换模板中的 {1}, {2}...
     */
    fun send(phoneNumbers: List<String>, templateId: String, templateParams: List<String>)

    /**
     * 根据模板和参数计算短信内容和条数
     * @param templateId 模板ID
     * @param templateParams 模板参数列表，按顺序替换模板中的 {1}, {2}...
     * @return 计算结果，包含渲染后的内容和最终的短信条数
     */
    fun calculate(templateId: String, templateParams: List<String>): SmsCalculationResult
}
