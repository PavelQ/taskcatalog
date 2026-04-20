package ru.pavelq.taskcatalog.model

enum class TaskStatus(val id: Int) {
    NEW(1),
    IN_PROGRESS(2),
    DONE(3),
    CANCELLED(4)
}
