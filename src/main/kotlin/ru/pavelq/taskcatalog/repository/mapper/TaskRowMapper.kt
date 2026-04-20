package ru.pavelq.taskcatalog.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.pavelq.taskcatalog.model.Task
import ru.pavelq.taskcatalog.model.TaskStatus
import java.sql.ResultSet
import java.time.LocalDateTime

@Component
class TaskRowMapper : RowMapper<Task> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Task = Task(
        id = rs.getLong("id"),
        title = rs.getString("title"),
        description = rs.getString("description"),
        status = TaskStatus.entries.first { it.id == rs.getInt("status_id") },
        createdAt = rs.getObject("created_at", LocalDateTime::class.java),
        updatedAt = rs.getObject("updated_at", LocalDateTime::class.java)
    )
}