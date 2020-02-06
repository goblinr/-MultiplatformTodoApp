package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.DefaultStateSubjectProvider
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.freeze

class TodoReducer : Reducer<TodoState, TodoAction> {

    override fun reduce(state: TodoState, action: TodoAction): TodoState = when (action) {
        TodoAction.Idle,
        TodoAction.CreateTask -> state
        is TodoAction.Switch,
        TodoAction.Archive,
        is TodoAction.Unarchive,
        TodoAction.Load -> state.copy(isLoading = true)
        is TodoAction.Loaded -> state.copy(
            isLoading = false,
            todoList = action.tasks,
            showArchive = action.tasks.fold(false) { result, task ->
                result || task.status == TaskStatus.DONE
            },
            error = ""
        )
        is TodoAction.Error -> state.copy(
            isLoading = false,
            error = action.message
        )
    }.freeze()
}

class TodoStateProvider(private val restoredState: TodoState?) : StateProvider<TodoState> {

    override fun get(): TodoState = restoredState?.freeze() ?: TodoState().freeze()
}

class TodoStore(
    reducer: Reducer<TodoState, TodoAction>,
    middleware: Set<Middleware<TodoAction, TodoState>>,
    initialState: StateProvider<TodoState>,
    stateSubjectProvider: StateSubjectProvider<TodoState> = DefaultStateSubjectProvider()
) : Store<TodoState, TodoAction>(reducer, middleware, initialState, stateSubjectProvider) {

    init {
        acceptAction(TodoAction.Load)
    }
}
