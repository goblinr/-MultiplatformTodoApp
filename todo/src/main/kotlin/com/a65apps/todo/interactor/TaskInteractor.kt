package com.a65apps.todo.interactor

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.todo.entity.ErrorResponse
import com.a65apps.todo.entity.TaskPost
import com.a65apps.todo.reporitory.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class TaskInteractor @Autowired constructor(
    private val repository: TaskRepository
) {

    fun tasks(): ResponseEntity<List<Task>> = ResponseEntity.status(HttpStatus.OK)
        .body(
            repository.tasks()
                .filter { it.status != TaskStatus.ARCHIVED }
        )

    fun createTask(task: TaskPost): ResponseEntity<Any> {
        if (task.title.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse("Title should't be empty"))
        }
        if (task.description.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse("Description should't be empty"))
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(repository.save(task))
    }

    fun switchTask(id: String): ResponseEntity<Any> {
        val task = repository.getTask(id)
        if (task != null) {
            val switchedTask = task.copy(
                status = if (task.status == TaskStatus.PENDING) TaskStatus.DONE else
                    TaskStatus.PENDING
            )
            return ResponseEntity.status(HttpStatus.OK)
                .body(repository.save(switchedTask))
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse("Not found"))
    }

    fun archiveTasks(): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.OK)
            .body(
                repository.save(repository.tasks()
                    .filter { it.status == TaskStatus.DONE }
                    .map {
                        it.copy(status = TaskStatus.ARCHIVED)
                    })
            )
}
