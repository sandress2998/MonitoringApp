package ru.mephi.monitoringapp.model.exception

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionResolver (
    private val registry: MeterRegistry
) {
    val internalError = Counter.builder("api.error")
        .description("Total quantity of errors in requests.")  // Описание метрики // Второй тег
        .tag("status", HttpStatus.INTERNAL_SERVER_ERROR.toString())
        .register(registry)                     // Регистрация в реестре

    val notAcceptableError = Counter.builder("api.error")
        .description("Total quantity of errors in requests.")  // Описание метрики // Второй тег
        .tag("status", HttpStatus.NOT_ACCEPTABLE.toString())
        .register(registry)                     // Регистрация в реестре


    val notFoundError = Counter.builder("api.error")
        .description("Total quantity of errors in requests.")  // Описание метрики // Второй тег
        .tag("status", HttpStatus.NOT_FOUND.toString())
        .register(registry)                     // Регистрация в реестре

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message,
            status = HttpStatus.NOT_FOUND.value()
        )
        notFoundError.increment()
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NotAcceptableException::class)
    fun handleNotFound(ex: NotAcceptableException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message,
            status = HttpStatus.NOT_ACCEPTABLE.value()
        )
        notAcceptableError.increment()
        return ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleNotFound(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message ?: "Not Found",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        internalError.increment()
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}