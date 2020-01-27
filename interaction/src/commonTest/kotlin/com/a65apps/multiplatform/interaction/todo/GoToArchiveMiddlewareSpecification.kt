package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockStore
import com.a65apps.multiplatform.interaction.mock.verifyReduce
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class GoToArchiveMiddlewareSpecification : BaseMiddlewareSpecification<TodoAction, TodoState>() {

    val state = MainState()
    lateinit var store: Store<MainState, MainAction>
    lateinit var mainReducer: Reducer<MainState, MainAction>

    override val defaultState = TodoState()

    override fun initMocks() {
        store = mockStore(state) {
            stubReduce(eq(state), eq(MainAction.NavigateForward(Screen.ARCHIVE_LIST)), state)
            mainReducer = this
        }
    }

    override fun createMiddleware(): Middleware<TodoAction, TodoState> =
        GoToArchiveMiddleware(store)

    @Test
    fun `it should navigate forward to ARCHIVE_LIST screen on GoToArchive action`() {
        val observer = bind()

        acceptAction(TodoAction.GoToArchive)

        observer.assertValue(TodoAction.Idle)
        mainReducer.verifyReduce<MainState, MainAction>(
            eq(state), eq(MainAction.NavigateForward(Screen.ARCHIVE_LIST))
        )
    }

    @Test
    fun `it should't respond to any action except GoToArchive`() {
        val observer = bind()

        acceptAction(TodoAction.Idle)
        acceptAction(TodoAction.Archive)
        acceptAction(TodoAction.Unarchive("id"))
        acceptAction(TodoAction.Load)
        acceptAction(TodoAction.Error("error"))
        acceptAction(TodoAction.Loaded(listOf()))
        acceptAction(TodoAction.Switch("id"))

        observer.assertNoValues()
    }
}
