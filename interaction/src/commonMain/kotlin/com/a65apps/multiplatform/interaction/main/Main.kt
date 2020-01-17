package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.DefaultStateSubjectProvider
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.freeze
import com.a65apps.multiplatform.interaction.navigation.Screen

class MainReducer : Reducer<MainState, MainAction> {

    override fun reduce(state: MainState, action: MainAction): MainState = when (action) {
        is MainAction.NavigateForward -> state.copy(
            screen = action.screen,
            backStack = state.backStack + state.screen
        ).freeze()
        is MainAction.NavigateReplace -> state.copy(
            screen = action.screen
        ).freeze()
        MainAction.NavigateBack -> state.copy(
            screen = state.backStack.lastOrNull() ?: state.screen,
            backStack = state.backStack.take((state.backStack.size - 1)
                .takeIf { state.backStack.isNotEmpty() } ?: 0)
        ).freeze()
        MainAction.Idle -> state.freeze()
    }
}

class MainStateProvider(private val restoredState: MainState?) : StateProvider<MainState> {

    override fun get(): MainState = restoredState?.freeze() ?: MainState().freeze()
}

class MainStore(
    reducer: Reducer<MainState, MainAction>,
    middleware: Set<Middleware<MainAction, MainState>>,
    initialState: StateProvider<MainState>,
    stateSubjectProvider: StateSubjectProvider<MainState> = DefaultStateSubjectProvider(),
    restoredState: MainState?
) : Store<MainState, MainAction>(reducer, middleware, initialState, stateSubjectProvider) {

    init {
        if (restoredState == null) {
            acceptAction(MainAction.NavigateReplace(Screen.TODO_LIST))
        }
    }
}
