package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.Action

sealed class TodoAction : Action {

    object Idle : TodoAction()

    data class Switch(val id: String) : TodoAction()

    object Archive : TodoAction()

    data class Unarchive(val id: String) : TodoAction()

    object Load : TodoAction()

    data class Loaded(val tasks: List<Task>) : TodoAction()

    data class Error(val message: String) : TodoAction()

    object CreateTask : TodoAction()

    object GoToArchive : TodoAction()
}
