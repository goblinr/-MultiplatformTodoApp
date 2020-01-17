package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.Action

enum class FieldType {
    TITLE,
    DESCRIPTION
}

sealed class CreateAction : Action {

    object Idle : CreateAction()

    object Create : CreateAction()

    data class CreateResult(val task: Task) : CreateAction()

    data class Error(val message: String) : CreateAction()

    data class UpdateField(
        val value: String,
        val type: FieldType
    ) : CreateAction()
}
