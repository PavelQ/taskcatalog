package ru.pavelq.taskcatalog.integration.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import ru.pavelq.taskcatalog.dto.TaskRequest
import ru.pavelq.taskcatalog.dto.TaskStatusRequest
import ru.pavelq.taskcatalog.dto.TaskUpdateRequest
import ru.pavelq.taskcatalog.exception.TaskNotFoundException
import ru.pavelq.taskcatalog.integration.BaseIntegrationTest
import ru.pavelq.taskcatalog.model.TaskStatus
import ru.pavelq.taskcatalog.service.TaskService

class TaskServiceIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var service: TaskService

    @Test
    fun `should create task with NEW status`() {
        // Given
        val request = TaskRequest("Test Task", "Description")

        // When & Then
        StepVerifier.create(service.createTask(request))
            .assertNext { task ->
                assertThat(task.title).isEqualTo("Test Task")
                assertThat(task.description).isEqualTo("Description")
                assertThat(task.status).isEqualTo("NEW")
            }
            .verifyComplete()
    }

    @Test
    fun `should update task status`() {
        // Given
        val request = TaskRequest("Test Task", "Description")

        // When & Then
        StepVerifier.create(
            service.createTask(request)
                .flatMap { service.updateStatus(it.id, TaskStatusRequest(TaskStatus.IN_PROGRESS)) }
                .flatMap { service.getTaskById(it.id) }
        )
            .assertNext { task ->
                assertThat(task.status).isEqualTo("IN_PROGRESS")
            }
            .verifyComplete()
    }

    @Test
    fun `should throw TaskNotFoundException when updating non-existent task status`() {
        // Given
        val request = TaskStatusRequest(TaskStatus.DONE)

        // When & Then
        StepVerifier.create(service.updateStatus(99999L, request))
            .expectError(TaskNotFoundException::class.java)
            .verify()
    }

    @Test
    fun `should update task details`() {
        // Given
        val request = TaskRequest("Original Title", "Original Desc")

        // When & Then
        StepVerifier.create(
            service.createTask(request)
                .flatMap { service.updateTask(it.id, TaskUpdateRequest("Updated Title", "Updated Desc")) }
                .flatMap { service.getTaskById(it.id) }
        )
            .assertNext { task ->
                assertThat(task.title).isEqualTo("Updated Title")
                assertThat(task.description).isEqualTo("Updated Desc")
            }
            .verifyComplete()
    }

    @Test
    fun `should delete task by id`() {
        // Given
        val request = TaskRequest("To Be Deleted", "Desc")

        // When & Then
        StepVerifier.create(
            service.createTask(request)
                .flatMap { task -> service.deleteTask(task.id).thenReturn(task.id) }
                .flatMap { id -> service.getTaskById(id) }
        )
            .expectError(TaskNotFoundException::class.java)
            .verify()
    }
}
