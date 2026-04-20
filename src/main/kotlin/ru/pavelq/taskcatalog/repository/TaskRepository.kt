package ru.pavelq.taskcatalog.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import ru.pavelq.taskcatalog.model.Task
import ru.pavelq.taskcatalog.model.TaskStatus
import ru.pavelq.taskcatalog.repository.mapper.TaskRowMapper

@Repository
class TaskRepository(
    private val jdbcClient: JdbcClient,
    private val rowMapper: TaskRowMapper
) {

    private val log = KotlinLogging.logger {}

    fun save(title: String, description: String?): Task {
        log.debug { "Saving new task with title: $title" }
        return jdbcClient.sql(
            """
            INSERT INTO tasks (title, description)
            VALUES (:title, :description)
            RETURNING id, title, description, status_id, created_at, updated_at
            """.trimIndent()
        )
            .param("title", title)
            .param("description", description)
            .query(rowMapper)
            .single()
    }

    fun findById(id: Long): Task? {
        log.debug { "Finding task by id: $id" }
        return jdbcClient.sql(
            """
            SELECT id, title, description, status_id, created_at, updated_at
            FROM tasks
            WHERE id = :id
            """.trimIndent()
        )
            .param("id", id)
            .query(rowMapper)
            .optional()
            .orElse(null)
    }

    fun findAll(page: Int, size: Int, status: TaskStatus?): List<Task> {
        log.debug { "Finding tasks: page=$page, size=$size, status=$status" }
        return jdbcClient.sql(
            """
            SELECT id, title, description, status_id, created_at, updated_at
            FROM tasks
            WHERE (CAST(:statusId AS INTEGER) IS NULL OR status_id = :statusId)
            ORDER BY created_at DESC
            LIMIT :limit OFFSET :offset
            """.trimIndent()
        )
            .param("statusId", status?.id)
            .param("limit", size)
            .param("offset", page * size)
            .query(rowMapper)
            .list()
    }

    fun countAll(status: TaskStatus?): Long {
        log.debug { "Counting tasks with status: $status" }
        return jdbcClient.sql(
            """
            SELECT COUNT(*)
            FROM tasks
            WHERE (CAST(:statusId AS INTEGER) IS NULL OR status_id = :statusId)
            """.trimIndent()
        )
            .param("statusId", status?.id)
            .query(Long::class.java)
            .single()
    }

    fun updateStatus(id: Long, status: TaskStatus): Task? {
        log.debug { "Updating status of task $id to $status" }
        return jdbcClient.sql(
            """
            UPDATE tasks
            SET status_id  = :statusId,
                updated_at = NOW()
            WHERE id = :id
            RETURNING id, title, description, status_id, created_at, updated_at
            """.trimIndent()
        )
            .param("statusId", status.id)
            .param("id", id)
            .query(rowMapper)
            .optional()
            .orElse(null)
    }

    fun updateTask(id: Long, title: String?, description: String?): Task? {
        log.debug { "Updating task $id: title=$title, description=$description" }
        return jdbcClient.sql(
            """
            UPDATE tasks
            SET title       = COALESCE(:title, title),
                description = COALESCE(:description, description),
                updated_at  = NOW()
            WHERE id = :id
            RETURNING id, title, description, status_id, created_at, updated_at
            """.trimIndent()
        )
            .param("title", title)
            .param("description", description)
            .param("id", id)
            .query(rowMapper)
            .optional()
            .orElse(null)
    }

    fun deleteById(id: Long): Int {
        log.debug { "Deleting task by id: $id" }
        return jdbcClient.sql("DELETE FROM tasks WHERE id = :id")
            .param("id", id)
            .update()
    }
}
