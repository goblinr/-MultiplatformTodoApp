package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.State

data class CreateState(
    val isLoading: Boolean = false,
    val error: String = "",
    val title: String = "",
    val description: String = "",
    val result: Task? = null
) : State
