package com.a65apps.todo.reporitory

import com.a65apps.multiplatform.domain.Task
import com.a65apps.todo.entity.TaskPost

interface TaskRepository {

    fun tasks(): List<Task>

    fun save(task: TaskPost): Task

    fun save(task: Task): Task

    fun save(tasks: List<Task>)

    fun getTask(id: String): Task?
}
