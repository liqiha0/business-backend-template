package io.github.liqiha0.template.core.domain.shared

class BusinessException(message: String? = null, val code: String? = null, cause: Throwable? = null) :
    RuntimeException(message, cause) {
}