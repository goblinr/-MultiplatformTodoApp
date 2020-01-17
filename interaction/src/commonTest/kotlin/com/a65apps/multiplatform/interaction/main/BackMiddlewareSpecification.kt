package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.mock.mockRouter
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyBack
import com.a65apps.multiplatform.interaction.navigation.Router
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class BackMiddlewareSpecification : BaseMiddlewareSpecification<MainAction, MainState>() {

    lateinit var router: Router

    override val defaultState = MainState()

    override fun initMocks() {
        router = mockRouter()
    }

    override fun createMiddleware(): Middleware<MainAction, MainState> =
        BackMiddleware(router, schedulers)

    @Test
    fun `it should call router back method on NavigateBack action`() {
        router.stubbing {
            stubBack()
        }
        val observer = bind()

        acceptAction(MainAction.NavigateBack)

        observer.assertValue(MainAction.Idle)
        router.verifyBack()
    }

    @Test
    fun `it should't respond to any action except NavigateBack`() {
        val observer = bind()

        acceptAction(MainAction.Idle)
        acceptAction(MainAction.NavigateForward(Screen.CREATE_TASK))
        acceptAction(MainAction.NavigateReplace(Screen.CREATE_TASK))

        observer.assertNoValues()
    }
}
