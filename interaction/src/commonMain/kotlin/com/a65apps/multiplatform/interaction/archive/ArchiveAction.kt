package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.Action

sealed class ArchiveAction : Action {

    object Idle : ArchiveAction()

    object Load : ArchiveAction()

    data class Loaded(val tasks: List<Task>) : ArchiveAction()

    data class UnarchiveTask(val id: String) : ArchiveAction()

    data class Error(val message: String) : ArchiveAction()
}
