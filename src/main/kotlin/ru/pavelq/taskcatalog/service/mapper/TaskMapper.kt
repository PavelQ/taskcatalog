package ru.pavelq.taskcatalog.service.mapper

import ru.pavelq.taskcatalog.dto.TaskResponse
import ru.pavelq.taskcatalog.model.Task

fun Task.toResponse(): TaskResponse = TaskResponse(
    id = id,
    title = title,
    description = description,
    status = status.name,
    createdAt = createdAt,
    updatedAt = updatedAt
)
