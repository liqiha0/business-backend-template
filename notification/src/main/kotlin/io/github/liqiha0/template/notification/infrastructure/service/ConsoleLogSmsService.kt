package io.github.liqiha0.template.notification.infrastructure.service

import io.github.liqiha0.template.notification.domain.service.SmsCalculationResult
import io.github.liqiha0.template.notification.domain.service.SmsService
import org.slf4j.LoggerFactory

class ConsoleLogSmsService : SmsService {

    private val logger = LoggerFactory.getLogger(ConsoleLogSmsService::class.java)

    override fun send(phoneNumbers: List<String>, templateId: String, templateParams: List<String>) {
        logger.info("--- [SMS SIMULATION] ---")
        logger.info("Provider: CONSOLE_LOG")
        logger.info("Recipients: ${phoneNumbers.joinToString(", ")}")
        logger.info("Template ID: $templateId")
        logger.info("Parameters: $templateParams")
        logger.info("--- [END OF SMS SIMULATION] ---")
    }

    override fun calculate(templateId: String, templateParams: List<String>): SmsCalculationResult {
        val content = "[Simulated Content for $templateId with params $templateParams]"
        // 模拟一个简单的计费规则
        val count = if (content.length > 70) 2 else 1
        logger.info("Calculating SMS count for template $templateId. Result: $count")
        return SmsCalculationResult(content, count)
    }
}
