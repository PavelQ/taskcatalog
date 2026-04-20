package ru.pavelq.taskcatalog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskcatalogApplication

fun main(args: Array<String>) {
    runApplication<TaskcatalogApplication>(*args)
}
