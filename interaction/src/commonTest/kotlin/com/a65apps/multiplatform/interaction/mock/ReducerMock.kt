package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.State
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReducerMock<S : State, A : Action> : Reducer<S, A>, Mock<MatcherKey2<S, A>, S> {

    override var keys = listOf<MatcherKey2<S, A>>()
    override var stubs = mapOf<MatcherKey2<S, A>, List<S>>()
    override val invocations = AtomicReference<Map<MatcherKey2<S, A>, Int>>(mapOf())

    override fun reduce(state: S, action: A): S = stub(
        mock = this,
        first = state,
        second = action
    )

    fun stubReduce(
        state: Matcher<S>,
        action: Matcher<A>,
        result: S
    ) {
        stubMethod(this, state, action, result)
    }

    fun verifyReduce(
        state: Matcher<S>,
        action: Matcher<A>,
        count: Int
    ) {
        assertEquals(
            expected = count,
            actual = invocations.value[MatcherKey2(state, action)]
        )
    }
}

fun <S : State, A : Action> mockReducer(
    body: ReducerMock<S, A>.() -> Unit = {}
): Reducer<S, A> {
    val mock = ReducerMock<S, A>()
    body(mock)

    return mock
}

fun <S : State, A : Action> Reducer<S, A>.stubbing(
    body: ReducerMock<S, A>.() -> Unit
) {
    assertTrue(actual = this is ReducerMock, message = "$this is not mocked")

    body(this)
}

fun <S : State, A : Action> Reducer<S, A>.verifyReduce(
    state: Matcher<S>,
    action: Matcher<A>,
    count: Int = 1
) {
    assertTrue(actual = this is ReducerMock, message = "$this is not mocked")

    verifyReduce(state, action, count)
}
