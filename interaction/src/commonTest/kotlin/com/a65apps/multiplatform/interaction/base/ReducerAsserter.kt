package com.a65apps.multiplatform.interaction.base

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.State
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReducerAsserter<S : State, A : Action>(
    private val reducer: Reducer<S, A>,
    private val givenState: S,
    private val givenAction: A,
    private val expectedState: S
) {

    fun assert() {
        val result = reducer.reduce(
            state = givenState,
            action = givenAction
        )

        assertEquals(
            expected = expectedState,
            actual = result,
            message = "actual state: $result is not equals to expected: $expectedState"
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "actual state is not frozen"
        )
    }
}

fun <S : State, A : Action> Reducer<S, A>.assert(
    givenState: S,
    givenAction: A,
    expectedState: S
) {
    ReducerAsserter(
        reducer = this,
        givenState = givenState,
        givenAction = givenAction,
        expectedState = expectedState
    ).assert()
}
