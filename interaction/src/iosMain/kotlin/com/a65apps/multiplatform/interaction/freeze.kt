package com.a65apps.multiplatform.interaction

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.badoo.reaktive.utils.freeze

actual fun <T : State> T.freeze(): T = this.freeze()

fun freezeTaskList(list: List<Task>): List<Task> = list.map {
    it.copy().freeze()
}.freeze()

fun createTask(
    id: String,
    title: String,
    description: String,
    status: String
): Task = Task(
    id = id,
    title = title,
    description = description,
    status = TaskStatus.valueOf(status)
).freeze()
