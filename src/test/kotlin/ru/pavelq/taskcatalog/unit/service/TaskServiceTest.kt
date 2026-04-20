package ru.pavelq.taskcatalog.unit.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import ru.pavelq.taskcatalog.dto.TaskRequest
import ru.pavelq.taskcatalog.dto.TaskStatusRequest
import ru.pavelq.taskcatalog.exception.TaskNotFoundException
import ru.pavelq.taskcatalog.model.TaskStatus
import ru.pavelq.taskcatalog.repository.TaskRepository
import ru.pavelq.taskcatalog.service.TaskService
import ru.pavelq.taskcatalog.unit.BaseUnitTest

class TaskServiceTest : BaseUnitTest() {

    private val repository = mockk<TaskRepository>()
    private val service = TaskService(repository)

    @Test
    fun `createTask should save and return task response`() {
        // Given
        val request = TaskRequest("Test Task", "Description")
        every { repository.save(any(), any()) } returns sampleTask

        // When
        val result = service.createTask(request)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectNextMatches { it.id == 1L && it.title == "Test Task" }
            .verifyComplete()

        verify { repository.save("Test Task", "Description") }
    }

    @Test
    fun `getTaskById should return task when found`() {
        // Given
        every { repository.findById(1L) } returns sampleTask

        // When
        val result = service.getTaskById(1L)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectNextMatches { it.id == 1L }
            .verifyComplete()
    }

    @Test
    fun `getTaskById should throw TaskNotFoundException when not found`() {
        // Given
        every { repository.findById(1L) } returns null

        // When
        val result = service.getTaskById(1L)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectError(TaskNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `updateStatus should return updated task response`() {
        // Given
        val request = TaskStatusRequest(TaskStatus.IN_PROGRESS)
        val updatedTask = sampleTask.copy(status = TaskStatus.IN_PROGRESS)
        every { repository.updateStatus(1L, TaskStatus.IN_PROGRESS) } returns updatedTask

        // When
        val result = service.updateStatus(1L, request)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectNextMatches { TaskStatus.IN_PROGRESS.name == it.status }
            .verifyComplete()
    }

    @Test
    fun `updateStatus should throw error when task not found`() {
        // Given
        every { repository.updateStatus(1L, any()) } returns null

        // When
        val result = service.updateStatus(1L, TaskStatusRequest(TaskStatus.DONE))

        // Then
        result
            .`as`(StepVerifier::create)
            .expectError(TaskNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `deleteTask should complete successfully`() {
        // Given
        every { repository.deleteById(1L) } returns 1

        // When
        val result = service.deleteTask(1L)

        // Then
        result
            .`as`(StepVerifier::create)
            .verifyComplete()

        verify { repository.deleteById(1L) }
    }

    @Test
    fun `deleteTask should throw error when nothing to delete`() {
        // Given
        every { repository.deleteById(1L) } returns 0

        // When
        val result = service.deleteTask(1L)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectError(TaskNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `getTasks should return page response`() {
        // Given
        every { repository.findAll(0, 10, TaskStatus.NEW) } returns listOf(sampleTask)
        every { repository.countAll(TaskStatus.NEW) } returns 1L

        // When
        val result = service.getTasks(0, 10, TaskStatus.NEW)

        // Then
        result
            .`as`(StepVerifier::create)
            .expectNextMatches {
                it.content.size == 1 && it.totalElements == 1L && it.totalPages == 1
            }
            .verifyComplete()
    }
}
