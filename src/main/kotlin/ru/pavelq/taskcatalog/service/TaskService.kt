package ru.pavelq.taskcatalog.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.pavelq.taskcatalog.dto.*
import ru.pavelq.taskcatalog.exception.TaskNotFoundException
import ru.pavelq.taskcatalog.model.TaskStatus
import ru.pavelq.taskcatalog.repository.TaskRepository
import ru.pavelq.taskcatalog.service.mapper.toResponse
import kotlin.math.ceil

@Service
class TaskService(private val taskRepository: TaskRepository) {

    private val log = KotlinLogging.logger {}

    fun createTask(request: TaskRequest): Mono<TaskResponse> =
        Mono.fromCallable {
            log.info { "Creating task: title=${request.title}" }
            taskRepository.save(request.title, request.description).toResponse()
        }.subscribeOn(Schedulers.boundedElastic())

    fun getTaskById(id: Long): Mono<TaskResponse> =
        Mono.fromCallable {
            log.debug { "Fetching task by id: $id" }
            taskRepository.findById(id)?.toResponse()
                ?: throw TaskNotFoundException(id)
        }.subscribeOn(Schedulers.boundedElastic())

    fun getTasks(page: Int, size: Int, status: TaskStatus?): Mono<PageResponse<TaskResponse>> =
        Mono.fromCallable {
            log.debug { "Fetching tasks: page=$page, size=$size, status=$status" }
            val tasks = taskRepository.findAll(page, size, status)
            val total = taskRepository.countAll(status)
            val totalPages = if (total == 0L) 0 else ceil(total.toDouble() / size).toInt()
            PageResponse(
                content = tasks.map { it.toResponse() },
                page = page,
                size = size,
                totalElements = total,
                totalPages = totalPages
            )
        }.subscribeOn(Schedulers.boundedElastic())

    fun updateStatus(id: Long, request: TaskStatusRequest): Mono<TaskResponse> =
        Mono.fromCallable {
            log.info { "Updating status of task $id ${request.status}" }
            val updatedTask = taskRepository.updateStatus(id, request.status)
                ?: throw TaskNotFoundException(id)
            updatedTask.toResponse()
        }.subscribeOn(Schedulers.boundedElastic())

    fun updateTask(id: Long, request: TaskUpdateRequest): Mono<TaskResponse> =
        Mono.fromCallable {
            log.info { "Updating task $id: title=${request.title}" }
            val updatedTask = taskRepository.updateTask(id, request.title, request.description)
                ?: throw TaskNotFoundException(id)
            updatedTask.toResponse()
        }.subscribeOn(Schedulers.boundedElastic())

    fun deleteTask(id: Long): Mono<Void> =
        Mono.fromRunnable<Void> {
            log.info { "Deleting task $id" }
            val deleted = taskRepository.deleteById(id)
            if (deleted == 0) throw TaskNotFoundException(id)
        }.subscribeOn(Schedulers.boundedElastic())
}
