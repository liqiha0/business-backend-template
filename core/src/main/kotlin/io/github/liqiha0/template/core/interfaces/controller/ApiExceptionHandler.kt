package io.github.liqiha0.template.core.interfaces.controller

import io.github.liqiha0.template.core.domain.shared.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice(annotations = [ApiResponse::class])
class ApiExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)
    }

    @ExceptionHandler(BusinessException::class)
    fun businessExceptionHandler(exception: BusinessException): ResponseEntity<ErrorResponse> {
        val firstStackTrace = exception.stackTrace[0]
        logger.warn(
            "业务异常：{}.{} {}",
            firstStackTrace.className,
            firstStackTrace.methodName,
            exception.message
        )
        return ResponseEntity.unprocessableEntity()
            .body(ErrorResponse(code = exception.code, message = exception.message))
    }
}

data class ErrorResponse(val code: String? = null, val message: String?)
