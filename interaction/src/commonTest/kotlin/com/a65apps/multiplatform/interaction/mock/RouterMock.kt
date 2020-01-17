package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Router
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals

class RouterMock : Router {

    private val forwardMock = object : Mock<MatcherKey1<Route>, Unit> {
        override var keys: List<MatcherKey1<Route>> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey1<Route>, Int>>(mapOf())
        override var stubs: Map<MatcherKey1<Route>, List<Unit>> = mapOf()
    }

    private val replaceMock = object : Mock<MatcherKey1<Route>, Unit> {
        override var keys: List<MatcherKey1<Route>> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey1<Route>, Int>>(mapOf())
        override var stubs: Map<MatcherKey1<Route>, List<Unit>> = mapOf()
    }

    private val backMock = object : Mock<MatcherKey0, Unit> {
        override var keys: List<MatcherKey0> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey0, Int>>(mapOf())
        override var stubs: Map<MatcherKey0, List<Unit>> = mapOf()
    }

    override fun forward(route: Route) = stub(
        mock = forwardMock,
        first = route
    )

    override fun replace(route: Route) = stub(
        mock = replaceMock,
        first = route
    )

    override fun back() = stub(
        mock = backMock
    )

    fun stubForward(route: Matcher<Route>) {
        stubMethod(forwardMock, route, Unit)
    }

    fun stubReplace(route: Matcher<Route>) {
        stubMethod(replaceMock, route, Unit)
    }

    fun stubBack() {
        stubMethod(backMock, Unit)
    }

    fun verifyForward(route: Matcher<Route>, count: Int) {
        assertEquals(
            expected = count,
            actual = forwardMock.invocations.value[MatcherKey1(route)]
        )
    }

    fun verifyReplace(route: Matcher<Route>, count: Int) {
        assertEquals(
            expected = count,
            actual = replaceMock.invocations.value[MatcherKey1(route)]
        )
    }

    fun verifyBack(count: Int) {
        assertEquals(
            expected = count,
            actual = backMock.invocations.value[MatcherKey0]
        )
    }
}

fun mockRouter(): Router = RouterMock()

fun Router.stubbing(
    body: RouterMock.() -> Unit
) {
    kotlin.test.assertTrue(actual = this is RouterMock, message = "$this is not mocked")

    body(this)
}

fun Router.verifyForward(route: Matcher<Route>, count: Int = 1) {
    kotlin.test.assertTrue(actual = this is RouterMock, message = "$this is not mocked")

    this.verifyForward(route, count)
}

fun Router.verifyReplace(route: Matcher<Route>, count: Int = 1) {
    kotlin.test.assertTrue(actual = this is RouterMock, message = "$this is not mocked")

    this.verifyReplace(route, count)
}

fun Router.verifyBack(count: Int = 1) {
    kotlin.test.assertTrue(actual = this is RouterMock, message = "$this is not mocked")

    this.verifyBack(count)
}
