package com.a65apps.todo.controller

import com.a65apps.todo.entity.TaskPost
import com.a65apps.todo.interactor.TaskInteractor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController @Autowired constructor(
    private val interactor: TaskInteractor
) {

    @GetMapping("/tasks")
    fun tasks() = interactor.tasks()

    @PostMapping("/tasks")
    fun createTask(@RequestBody task: TaskPost) = interactor.createTask(task)

    @PatchMapping("/tasks/{id}")
    fun switchTask(@PathVariable id: String) = interactor.switchTask(id)

    @PostMapping("/tasks/archive")
    fun archiveTasks() = interactor.archiveTasks()
}
