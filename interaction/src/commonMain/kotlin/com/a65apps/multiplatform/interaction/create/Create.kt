package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.interaction.DefaultStateSubjectProvider
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.freeze

class CreateReducer : Reducer<CreateState, CreateAction> {

    override fun reduce(state: CreateState, action: CreateAction): CreateState = when (action) {
        CreateAction.Idle -> state.freeze()
        CreateAction.Create -> state.copy(isLoading = true, error = "").freeze()
        is CreateAction.Error -> state.copy(isLoading = false, error = action.message).freeze()
        is CreateAction.UpdateField -> when (action.type) {
            FieldType.TITLE -> state.copy(title = action.value).freeze()
            FieldType.DESCRIPTION -> state.copy(description = action.value).freeze()
        }
        is CreateAction.CreateResult -> state.copy(
            isLoading = false,
            error = "",
            result = action.task
        ).freeze()
    }
}

class CreateStateProvider(private val restoredState: CreateState?) : StateProvider<CreateState> {

    override fun get(): CreateState = restoredState?.freeze() ?: CreateState().freeze()
}

class CreateStore(
    reducer: Reducer<CreateState, CreateAction>,
    middleware: Set<Middleware<CreateAction, CreateState>>,
    initialState: StateProvider<CreateState>,
    stateSubjectProvider: StateSubjectProvider<CreateState> = DefaultStateSubjectProvider()
) : Store<CreateState, CreateAction>(reducer, middleware, initialState, stateSubjectProvider)
