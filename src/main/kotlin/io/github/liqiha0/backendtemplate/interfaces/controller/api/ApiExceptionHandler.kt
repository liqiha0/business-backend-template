package io.github.liqiha0.backendtemplate.interfaces.controller.api

import io.github.liqiha0.backendtemplate.domain.shared.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import kotlin.jvm.java

@ControllerAdvice(basePackageClasses = [ApiExceptionHandler::class])
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BusinessException::class)
    fun businessExceptionHandler(exception: BusinessException): ResponseEntity<ErrorResponse> {
        val firstStackTrace = exception.stackTrace[0]
        this.logger.warn(
            "API业务异常：{}.{} {}",
            firstStackTrace.className,
            firstStackTrace.methodName,
            exception.message
        )
        return ResponseEntity.unprocessableEntity()
            .body(ErrorResponse(code = exception.code, message = exception.message))
    }

    @ExceptionHandler(Exception::class)
    fun unknownExceptionHandler(exception: Exception): ResponseEntity<ErrorResponse> {
        this.logger.error("未知错误", exception)
        return ResponseEntity.internalServerError().body(ErrorResponse(message = exception.message))
    }
}

data class ErrorResponse(val code: String? = null, val message: String?)