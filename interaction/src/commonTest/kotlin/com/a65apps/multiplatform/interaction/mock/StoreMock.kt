package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.badoo.reaktive.subject.behavior.BehaviorSubject

class StoreMock<S : State, A : Action>(
    defaultState: S,
    body: ReducerMock<S, A>.() -> Unit
) : Store<S, A>(
    reducer = mockReducer(body),
    middleware = setOf(),
    initialState = mockStateProvider(),
    stateSubjectProvider = mockStateSubjectProvider {
        stubProvide(any(), BehaviorSubject(defaultState))
    }
)

fun <S : State, A : Action> mockStore(
    defaultState: S,
    body: ReducerMock<S, A>.() -> Unit = {}
): Store<S, A> = StoreMock(defaultState, body)
