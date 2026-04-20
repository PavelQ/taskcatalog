package ru.pavelq.taskcatalog.dto

import jakarta.validation.constraints.NotNull
import ru.pavelq.taskcatalog.model.TaskStatus

data class TaskStatusRequest(
    @field:NotNull(message = "Status must not be null")
    val status: TaskStatus
)
