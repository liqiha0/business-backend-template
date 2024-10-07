package io.github.liqiha0.template.core.interfaces.controller

import io.github.liqiha0.template.core.domain.shared.BusinessException
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


data class Vben5Result<T>(
    val data: T?,
    val code: Int,
    val message: String? = null,
    val error: String? = null,
)

fun <T> vben5Success(data: T? = null): Vben5Result<T> {
    return Vben5Result(data, 0, "ok")
}

fun <T> vben5Error(message: String? = null, code: Int = -1): Vben5Result<T> {
    return Vben5Result(null, code, message)
}

annotation class Vben5Response

@ControllerAdvice(annotations = [Vben5Response::class])
class Vben5ResponseWrapper : ResponseBodyAdvice<Any> {
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
            if (body is Vben5Result<*>) return body
            return vben5Success(body)
        } catch (e: Exception) {
            return vben5Error<Unit>(e.message)
        }
    }

    @ExceptionHandler(BusinessException::class)
    fun businessException(e: BusinessException): ResponseEntity<Vben5Result<Unit>> {
        val firstStackTrace = e.stackTrace[0]
        this.logger.warn(
            "Vben5业务异常：{}.{} {}",
            firstStackTrace.className,
            firstStackTrace.methodName,
            e.message
        )
        return ResponseEntity.ok(vben5Error(e.message))
    }
}
