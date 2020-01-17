package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.State
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MiddlewareMock<A : Action, S : State> : Middleware<A, S>,
    Mock<MatcherKey2<Observable<A>, Observable<S>>, Observable<A>> {

    override var keys = listOf<MatcherKey2<Observable<A>, Observable<S>>>()
    override var stubs = mapOf<MatcherKey2<Observable<A>, Observable<S>>, List<Observable<A>>>()
    override val invocations = AtomicReference<Map<MatcherKey2<Observable<A>, Observable<S>>, Int>>(
        mapOf())

    override fun bind(actions: Observable<A>, state: Observable<S>): Observable<A> = stub(
        mock = this,
        first = actions,
        second = state
    )

    fun stubBind(
        actions: Matcher<Observable<A>>,
        state: Matcher<Observable<S>>,
        result: Observable<A>
    ) {
        stubMethod(this, actions, state, result)
    }

    fun verifyBind(
        actions: Matcher<Observable<A>>,
        state: Matcher<Observable<S>>,
        count: Int
    ) {
        assertEquals(
            expected = count,
            actual = invocations.value[MatcherKey2(actions, state)]
        )
    }
}

fun <A : Action, S : State> mockMiddleware(): Middleware<A, S> = MiddlewareMock()

fun <S : State, A : Action> Middleware<A, S>.stubbing(
    body: MiddlewareMock<A, S>.() -> Unit
) {
    assertTrue(actual = this is MiddlewareMock, message = "$this is not mocked")

    body(this)
}

fun <S : State, A : Action> Middleware<A, S>.verifyBind(
    actions: Matcher<Observable<A>>,
    state: Matcher<Observable<S>>,
    count: Int = 1
) {
    assertTrue(actual = this is MiddlewareMock, message = "$this is not mocked")

    verifyBind(actions, state, count)
}
