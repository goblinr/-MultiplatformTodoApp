package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StateSubjectProviderMock<S : State> : StateSubjectProvider<S>,
    Mock<MatcherKey1<StateProvider<S>>, Subject<S>> {

    override var keys = listOf<MatcherKey1<StateProvider<S>>>()
    override var stubs = mapOf<MatcherKey1<StateProvider<S>>, List<Subject<S>>>()
    override val invocations = AtomicReference<Map<MatcherKey1<StateProvider<S>>, Int>>(mapOf())

    override fun provide(initialState: StateProvider<S>): Subject<S> = stub(
        mock = this,
        first = initialState
    )

    fun stubProvide(
        initialState: Matcher<StateProvider<S>>,
        result: Subject<S>
    ) {
        stubMethod(this, initialState, result)
    }

    fun verifyProvide(
        initialState: Matcher<StateProvider<S>>,
        count: Int
    ) {
        assertEquals(
            expected = count,
            actual = invocations.value[MatcherKey1(initialState)]
        )
    }
}

fun <S : State> mockStateSubjectProvider(
    body: StateSubjectProviderMock<S>.() -> Unit = {}
): StateSubjectProvider<S> {
    val mock = StateSubjectProviderMock<S>()
    body(mock)

    return mock
}

fun <S : State> StateSubjectProvider<S>.stubbing(
    body: StateSubjectProviderMock<S>.() -> Unit
) {
    assertTrue(actual = this is StateSubjectProviderMock, message = "$this is not mocked")

    body(this)
}

fun <S : State> StateSubjectProvider<S>.verifyProvide(
    initialState: Matcher<StateProvider<S>>,
    count: Int = 1
) {
    assertTrue(actual = this is StateSubjectProviderMock, message = "$this is not mocked")

    verifyProvide(initialState, count)
}
