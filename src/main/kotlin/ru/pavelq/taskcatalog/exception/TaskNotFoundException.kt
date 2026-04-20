package ru.pavelq.taskcatalog.exception

import org.springframework.http.HttpStatus

class TaskNotFoundException(id: Long) : TaskCatalogStatedException(
    message = "Task with id=$id not found",
    httpStatus = HttpStatus.NOT_FOUND
)
