package io.github.liqiha0.backendtemplate.interfaces.controller.vben

import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice


data class VbenResult<T>(
    val result: T?,
    val message: String?,
    val code: Int,
    val type: String,
)

fun <T> vbenSuccess(result: T? = null): VbenResult<T> {
    return VbenResult(result, "ok", 0, "success")
}

fun <T> vbenError(message: String? = null, code: Int = -1): VbenResult<T> {
    return VbenResult(null, message, code, "error")
}

annotation class VbenResponse

@ControllerAdvice(annotations = [VbenResponse::class])
class VbenResponseWrapper : ResponseBodyAdvice<Any> {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        try {
            if (body is VbenResult<*>) return body
            return vbenSuccess(body)
        } catch (e: Exception) {
            return vbenError<Unit>(e.message)
        }
    }

    @ExceptionHandler(BusinessException::class)
    fun businessException(e: BusinessException): ResponseEntity<VbenResult<Unit>> {
        val firstStackTrace = e.stackTrace[0]
        this.logger.warn(
            "API业务异常：{}.{} {}",
            firstStackTrace.className,
            firstStackTrace.methodName,
            e.message
        )
        return ResponseEntity.ok(vbenError(e.message))
    }

    @ExceptionHandler(Exception::class)
    fun unknownException(e: Exception): ResponseEntity<VbenResult<Unit>> {
        e.printStackTrace()
        return ResponseEntity.ok(vbenError(e.message))
    }
}