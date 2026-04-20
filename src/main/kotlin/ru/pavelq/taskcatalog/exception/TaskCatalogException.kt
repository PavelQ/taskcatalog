package ru.pavelq.taskcatalog.exception

open class TaskCatalogException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
