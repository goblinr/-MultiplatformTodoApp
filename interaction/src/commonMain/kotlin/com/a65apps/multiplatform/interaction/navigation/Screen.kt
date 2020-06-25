package com.a65apps.multiplatform.interaction.navigation

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.todo.TodoState

enum class Screen {
    TODO_LIST,
    CREATE_TASK,
    ARCHIVE_LIST
}

val DEFAULT_SCREEN: Screen = Screen.TODO_LIST
val DEFAULT_STATE: State = TodoState()
val DEFAULT_ACTIONS: (Action) -> Unit = {}
