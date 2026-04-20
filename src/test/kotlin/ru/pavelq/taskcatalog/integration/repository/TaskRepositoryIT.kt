package ru.pavelq.taskcatalog.integration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.pavelq.taskcatalog.integration.BaseIntegrationTest
import ru.pavelq.taskcatalog.repository.TaskRepository

class TaskRepositoryIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Test
    fun `should save and find task in real database`() {
        // Given
        val title = "Integrated Task"
        val description = "Testing with Testcontainers"

        // When
        val savedTask = taskRepository.save(title, description)
        val foundTask = taskRepository.findById(savedTask.id)

        // Then
        assertThat(foundTask).isNotNull
        assertThat(foundTask?.title).isEqualTo(title)
        assertThat(foundTask?.description).isEqualTo(description)
        assertThat(foundTask?.id).isEqualTo(savedTask.id)
    }

    @Test
    fun `should return empty when task not found`() {
        // When
        val foundTask = taskRepository.findById(-1L)

        // Then
        assertThat(foundTask).isNull()
    }
}
