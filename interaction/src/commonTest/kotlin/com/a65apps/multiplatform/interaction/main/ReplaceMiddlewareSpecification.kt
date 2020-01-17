package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockRouter
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyReplace
import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Router
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class ReplaceMiddlewareSpecification : BaseMiddlewareSpecification<MainAction, MainState>() {

    lateinit var router: Router

    override val defaultState = MainState()

    override fun initMocks() {
        router = mockRouter()
    }

    override fun createMiddleware(): Middleware<MainAction, MainState> =
        ReplaceMiddleware(router, schedulers)

    @Test
    fun `it should call router replace method with route parameter on NavigateReplace action`() {
        val expectedRoute = Route(
            screen = Screen.CREATE_TASK
        )
        router.stubbing {
            stubReplace(eq(expectedRoute))
        }
        val observer = bind()

        acceptAction(MainAction.NavigateReplace(screen = Screen.CREATE_TASK))

        observer.assertValue(MainAction.Idle)
        router.verifyReplace(eq(expectedRoute))
    }

    @Test
    fun `it should't respond to any action except NavigateReplace`() {
        val observer = bind()

        acceptAction(MainAction.Idle)
        acceptAction(MainAction.NavigateForward(Screen.CREATE_TASK))
        acceptAction(MainAction.NavigateBack)

        observer.assertNoValues()
    }
}
