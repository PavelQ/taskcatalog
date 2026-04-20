package ru.pavelq.taskcatalog.exception.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import ru.pavelq.taskcatalog.exception.TaskCatalogStatedException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(TaskCatalogStatedException::class)
    fun handleTaskCatalogStatedException(ex: TaskCatalogStatedException): ResponseEntity<ProblemDetail> {
        log.warn { "Business exception [${ex.httpStatus}]: ${ex.message}" }
        val problem = ProblemDetail.forStatusAndDetail(ex.httpStatus, ex.message ?: "Unexpected error")
        return ResponseEntity.status(ex.httpStatus).body(problem)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(ex: WebExchangeBindException): ResponseEntity<ProblemDetail> {
        val detail = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        log.debug { "Validation failed: $detail" }
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
        problem.title = "Validation Failed"
        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException): ResponseEntity<ProblemDetail> {
        val detail = ex.parameterValidationResults
            .flatMap { it.resolvableErrors }
            .joinToString(", ") { it.defaultMessage ?: "Invalid value" }
            .ifEmpty { ex.message }
        log.debug { "Method validation failed: $detail" }
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
        problem.title = "Validation Failed"
        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(org.springframework.web.server.ServerWebInputException::class)
    fun handleServerWebInputException(ex: org.springframework.web.server.ServerWebInputException): ResponseEntity<ProblemDetail> {
        log.debug { "Invalid input: ${ex.reason}" }
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.reason ?: "Invalid input")
        problem.title = "Bad Request"
        return ResponseEntity.badRequest().body(problem)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ProblemDetail> {
        log.error { "Unexpected error occurred: $ex" }
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
        )
        problem.title = "Internal Server Error"
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem)
    }
}