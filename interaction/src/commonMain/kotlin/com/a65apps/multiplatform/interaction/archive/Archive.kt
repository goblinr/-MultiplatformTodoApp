package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.interaction.DefaultStateSubjectProvider
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.freeze

class ArchiveReducer : Reducer<ArchiveState, ArchiveAction> {

    override fun reduce(state: ArchiveState, action: ArchiveAction): ArchiveState = when (action) {
        is ArchiveAction.Idle -> state
        is ArchiveAction.UnarchiveTask,
        ArchiveAction.Load -> state.copy(isLoading = true)
        is ArchiveAction.Loaded -> state.copy(
            isLoading = false,
            archiveTodoList = action.tasks,
            error = ""
        )
        is ArchiveAction.Error -> state.copy(
            isLoading = false,
            error = action.message
        )
    }.freeze()
}

class ArchiveStateProvider(private val restoredState: ArchiveState?) : StateProvider<ArchiveState> {

    override fun get(): ArchiveState = restoredState?.freeze() ?: ArchiveState().freeze()
}

class ArchiveStore(
    reducer: Reducer<ArchiveState, ArchiveAction>,
    middleware: Set<Middleware<ArchiveAction, ArchiveState>>,
    initialState: StateProvider<ArchiveState>,
    stateSubjectProvider: StateSubjectProvider<ArchiveState> = DefaultStateSubjectProvider()
) : Store<ArchiveState, ArchiveAction>(reducer, middleware, initialState, stateSubjectProvider) {

    init {
        acceptAction(ArchiveAction.Load)
    }
}
