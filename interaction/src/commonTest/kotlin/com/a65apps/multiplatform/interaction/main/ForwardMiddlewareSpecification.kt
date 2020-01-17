package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockRouter
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyForward
import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Router
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class ForwardMiddlewareSpecification : BaseMiddlewareSpecification<MainAction, MainState>() {

    lateinit var router: Router

    override val defaultState = MainState()

    override fun initMocks() {
        router = mockRouter()
    }

    override fun createMiddleware(): Middleware<MainAction, MainState> =
        ForwardMiddleware(router, schedulers)

    @Test
    fun `it should call router forward method with route parameter on NavigateForward action`() {
        val expectedRoute = Route(
            screen = Screen.CREATE_TASK
        )
        router.stubbing {
            stubForward(eq(expectedRoute))
        }
        val observer = bind()

        acceptAction(MainAction.NavigateForward(screen = Screen.CREATE_TASK))

        observer.assertValue(MainAction.Idle)
        router.verifyForward(eq(expectedRoute))
    }

    @Test
    fun `it should't respond to any action except NavigateForward`() {
        val observer = bind()

        acceptAction(MainAction.Idle)
        acceptAction(MainAction.NavigateReplace(Screen.CREATE_TASK))
        acceptAction(MainAction.NavigateBack)

        observer.assertNoValues()
    }
}
