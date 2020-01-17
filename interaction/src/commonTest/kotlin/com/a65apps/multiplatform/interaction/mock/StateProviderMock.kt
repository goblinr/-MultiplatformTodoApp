package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.StateProvider
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StateProviderMock<S : State> : StateProvider<S>, Mock<MatcherKey0, S> {

    override var keys: List<MatcherKey0> = listOf()
    override var stubs: Map<MatcherKey0, List<S>> = mapOf()
    override val invocations: AtomicReference<Map<MatcherKey0, Int>> = AtomicReference(mapOf())

    override fun get(): S = stub(
        mock = this
    )

    fun stub(value: S) {
        stubMethod(this, value)
    }

    fun verify(count: Int) {
        assertEquals(
            expected = count,
            actual = invocations.value[MatcherKey0]
        )
    }
}

fun <S : State> mockStateProvider(): StateProvider<S> = StateProviderMock()

fun <S : State> StateProvider<S>.stubbing(
    body: StateProviderMock<S>.() -> Unit
) {
    assertTrue(actual = this is StateProviderMock, message = "$this is not mocked")

    body(this)
}

fun <S : State> StateProvider<S>.verify(count: Int = 1) {
    assertTrue(actual = this is StateProviderMock, message = "$this is not mocked")

    verify(count)
}
