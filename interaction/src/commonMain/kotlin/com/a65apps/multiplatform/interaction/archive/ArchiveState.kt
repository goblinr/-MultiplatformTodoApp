package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.State

data class ArchiveState(
    val isLoading: Boolean = false,
    val error: String = "",
    val archiveTodoList: List<Task> = listOf()
) : State
