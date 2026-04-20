package ru.pavelq.taskcatalog.exception

import org.springframework.http.HttpStatus

open class TaskCatalogStatedException(
    message: String,
    val httpStatus: HttpStatus,
    cause: Throwable? = null
) : TaskCatalogException(message, cause)
