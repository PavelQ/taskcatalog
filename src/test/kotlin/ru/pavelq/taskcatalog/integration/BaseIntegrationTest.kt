package ru.pavelq.taskcatalog.integration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer
import ru.pavelq.taskcatalog.model.Task
import ru.pavelq.taskcatalog.model.TaskStatus
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIntegrationTest {

    companion object {
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:17-alpine")
            .withDatabaseName("taskcatalog")
            .withUsername("testuser")
            .withPassword("testpass")
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.flyway.url", postgres::getJdbcUrl)
            registry.add("spring.flyway.user", postgres::getUsername)
            registry.add("spring.flyway.password", postgres::getPassword)
        }
    }

    protected val fixedTime: LocalDateTime = LocalDateTime.of(2024, 1, 15, 10, 0, 0)

    protected val sampleTask = Task(
        id = 1L,
        title = "Test Task",
        description = "Description",
        status = TaskStatus.NEW,
        createdAt = fixedTime,
        updatedAt = fixedTime
    )
}
