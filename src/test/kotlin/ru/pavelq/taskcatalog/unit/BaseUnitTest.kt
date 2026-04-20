package ru.pavelq.taskcatalog.unit

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import ru.pavelq.taskcatalog.dto.TaskResponse
import ru.pavelq.taskcatalog.model.Task
import ru.pavelq.taskcatalog.model.TaskStatus
import java.time.LocalDateTime

abstract class BaseUnitTest {

    // Фиксированное время для стабильности тестов
    protected val fixedTime: LocalDateTime = LocalDateTime.of(2024, 1, 15, 10, 0, 0)


    protected val sampleTask = Task(
        id = 1L,
        title = "Test Task",
        description = "Description",
        status = TaskStatus.NEW,
        createdAt = fixedTime,
        updatedAt = fixedTime
    )

    protected val sampleTaskResponse = TaskResponse(
        id = 1L,
        title = "Test Task",
        description = "Description",
        status = "NEW",
        createdAt = fixedTime,
        updatedAt = fixedTime
    )

    protected val samplePageResponse = ru.pavelq.taskcatalog.dto.PageResponse(
        content = listOf(sampleTaskResponse),
        page = 0,
        size = 10,
        totalElements = 1L,
        totalPages = 1
    )

    @AfterEach
    fun tearDown() {
        // Очищаем историю вызовов и настройки моков после каждого теста
        clearAllMocks()
    }
}
