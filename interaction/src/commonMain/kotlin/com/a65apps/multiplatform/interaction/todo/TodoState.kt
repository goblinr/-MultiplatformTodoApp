package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.State

data class TodoState(
    val isLoading: Boolean = false,
    val error: String = "",
    val todoList: List<Task> = listOf(),
    val showArchive: Boolean = false
) : State
