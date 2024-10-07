package io.github.liqiha0.template.notification.infrastructure.service

import cn.ucloud.common.config.Config
import cn.ucloud.common.credential.Credential
import cn.ucloud.usms.client.USMSClient
import cn.ucloud.usms.models.QueryUSMSTemplateRequest
import cn.ucloud.usms.models.SendUSMSMessageRequest
import io.github.liqiha0.template.notification.domain.service.SmsCalculationResult
import io.github.liqiha0.template.notification.domain.service.SmsService
import io.github.liqiha0.template.notification.infrastructure.config.UCloudSmsProperties
import kotlin.math.ceil

class UCloudSmsService(private val properties: UCloudSmsProperties) : SmsService {

    private val client: USMSClient

    init {
        val config = Config()
        val credential = Credential(properties.publicKey, properties.privateKey)
        client = USMSClient(config, credential)
    }

    override fun send(phoneNumbers: List<String>, templateId: String, templateParams: List<String>) {
        val req = SendUSMSMessageRequest()
        req.projectId = properties.projectId
        req.phoneNumbers = phoneNumbers
        req.templateId = templateId
        req.templateParams = templateParams
        req.sigContent = properties.sigContent

        val resp = client.sendUSMSMessage(req)
        if (resp.retCode != 0) {
            throw RuntimeException("Failed to send SMS: ${resp.message}")
        }
    }

    override fun calculate(
        templateId: String,
        templateParams: List<String>
    ): SmsCalculationResult {
        val templateContent = getTemplateFromUCloud(templateId)
        val renderedContent = renderTemplate(templateContent, templateParams)
        val count = calculateSmsCount(renderedContent)

        return SmsCalculationResult(renderedContent, count)
    }

    private fun getTemplateFromUCloud(templateId: String): String {
        val req = QueryUSMSTemplateRequest()
        req.projectId = properties.projectId
        req.templateId = templateId

        try {
            val resp = client.queryUSMSTemplate(req)
            if (resp.retCode == 0) {
                return resp.data.template
            } else {
                throw RuntimeException("Failed to get UCloud SMS template: ${resp.message}")
            }
        } catch (e: Exception) {
            throw RuntimeException("Error while fetching UCloud SMS template", e)
        }
    }

    private fun renderTemplate(template: String, params: List<String>): String {
        var result = template
        params.forEachIndexed { index, value ->
            result = result.replace("{${index + 1}}", value)
        }
        return result
    }

    private fun calculateSmsCount(content: String): Int {
        val isUnicode = content.any { it.code > 127 }

        return if (isUnicode) {
            val singleSmsLength = 70
            val longSmsLength = 67
            if (content.length <= singleSmsLength) 1 else ceil(content.length.toDouble() / longSmsLength).toInt()
        } else {
            val singleSmsLength = 160
            val longSmsLength = 153
            if (content.length <= singleSmsLength) 1 else ceil(content.length.toDouble() / longSmsLength).toInt()
        }
    }
}
