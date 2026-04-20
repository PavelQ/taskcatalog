@file:Suppress("ReactiveStreamsUnusedPublisher")

package ru.pavelq.taskcatalog.unit.controller

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import ru.pavelq.taskcatalog.controller.TaskController
import ru.pavelq.taskcatalog.dto.TaskRequest
import ru.pavelq.taskcatalog.exception.TaskNotFoundException
import ru.pavelq.taskcatalog.exception.handler.GlobalExceptionHandler
import ru.pavelq.taskcatalog.service.TaskService
import ru.pavelq.taskcatalog.unit.BaseUnitTest

const val API_TASKS_PATH = "/api/tasks"

class TaskControllerTest : BaseUnitTest() {

    private val service = mockk<TaskService>()
    private lateinit var client: WebTestClient


    @BeforeEach
    fun setup() {
        client = WebTestClient.bindToController(TaskController(service))
            .controllerAdvice(GlobalExceptionHandler())
            .build()
    }

    @Test
    fun `createTask should return 201 Created`() {
        // Given
        val request = TaskRequest("Test Task", "Description")
        every { service.createTask(any()) } returns Mono.just(sampleTaskResponse)

        // When
        val result = client.post()
            .uri(API_TASKS_PATH)
            .bodyValue(request)
            .exchange()

        // Then
        result
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.title").isEqualTo("Test Task")
    }

    @Test
    fun `createTask should return 400 Bad Request when title is blank`() {
        // Given
        val request = TaskRequest("", "Desc")

        // When
        val result = client.post()
            .uri(API_TASKS_PATH)
            .bodyValue(request)
            .exchange()

        // Then
        result.expectStatus().isBadRequest
    }

    @Test
    fun `getTaskById should return 200 OK`() {
        // Given
        every { service.getTaskById(1L) } returns Mono.just(sampleTaskResponse)

        // When
        val result = client.get()
            .uri("$API_TASKS_PATH/1")
            .exchange()

        // Then
        result.expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
    }

    @Test
    fun `getTaskById should return 404 Not Found when service throws exception`() {
        // Given
        every { service.getTaskById(1L) } returns Mono.error(TaskNotFoundException(1L))

        // When
        val result = client.get()
            .uri("$API_TASKS_PATH/1")
            .exchange()

        // Then
        result.expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.title").isEqualTo("Not Found")
    }

    @Test
    fun `getTasks should return 200 OK`() {
        // Given
        every { service.getTasks(0, 10, null) } returns Mono.just(samplePageResponse)

        // When
        val result = client.get()
            .uri { it.path(API_TASKS_PATH).queryParam("page", 0).queryParam("size", 10).build() }
            .exchange()

        // Then
        result.expectStatus().isOk
            .expectBody()
            .jsonPath("$.content").isArray
    }

    @Test
    fun `getTasks should return 400 Bad Request when params missing`() {
        // When
        val result = client.get()
            .uri(API_TASKS_PATH)
            .exchange()

        // Then
        result.expectStatus().isBadRequest
    }

    @Test
    fun `updateStatus should return 200 OK`() {
        // Given
        val request = mapOf("status" to "DONE")
        every { service.updateStatus(1L, any()) } returns Mono.just(sampleTaskResponse)

        // When
        val result = client.patch()
            .uri("$API_TASKS_PATH/1/status")
            .bodyValue(request)
            .exchange()

        // Then
        result.expectStatus().isOk
    }

    @Test
    fun `updateTask should return 200 OK`() {
        // Given
        val request = ru.pavelq.taskcatalog.dto.TaskUpdateRequest("Updated Title", "Updated Desc")
        every { service.updateTask(1L, any()) } returns Mono.just(
            sampleTaskResponse.copy(
                title = "Updated Title",
                description = "Updated Desc"
            )
        )

        // When
        val result = client.patch()
            .uri("$API_TASKS_PATH/1")
            .bodyValue(request)
            .exchange()

        // Then
        result.expectStatus().isOk
            .expectBody()
            .jsonPath("$.title").isEqualTo("Updated Title")
            .jsonPath("$.description").isEqualTo("Updated Desc")
    }

    @Test
    fun `updateTask should return 400 Bad Request when title is blank or short`() {
        // Given - empty title validation violation
        val request = ru.pavelq.taskcatalog.dto.TaskUpdateRequest("", "Updated Desc")

        // When
        val result = client.patch()
            .uri("$API_TASKS_PATH/1")
            .bodyValue(request)
            .exchange()

        // Then
        result.expectStatus().isBadRequest
    }

    @Test
    fun `deleteTask should return 204 No Content`() {
        // Given
        every { service.deleteTask(1L) } returns Mono.empty()

        // When
        val result = client.delete()
            .uri("$API_TASKS_PATH/1")
            .exchange()

        // Then
        result.expectStatus().isNoContent
    }


}
