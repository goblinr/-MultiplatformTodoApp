package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.base.assert
import com.a65apps.multiplatform.interaction.navigation.Screen
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainReducerSpecification {

    lateinit var reducer: MainReducer

    @BeforeTest
    fun setUp() {
        reducer = MainReducer()
    }

    @Test
    fun `it should append last screen to back stack when navigate forward action emitted and set new screen`() {
        val givenState = MainState()

        reducer.assert(
            givenState = givenState,
            givenAction = MainAction.NavigateForward(
                screen = Screen.CREATE_TASK
            ),

            expectedState = MainState(
                screen = Screen.CREATE_TASK,
                backStack = listOf(givenState.screen)
            )
        )
    }

    @Test
    fun `it should replace current screen with screen from navigate replace action`() {
        val givenState = MainState()

        reducer.assert(
            givenState = givenState,
            givenAction = MainAction.NavigateReplace(
                screen = Screen.CREATE_TASK
            ),

            expectedState = MainState(
                screen = Screen.CREATE_TASK
            )
        )
    }

    @Test
    fun `it should't change state if navigation back stack is empty when back navigation performed`() {
        val givenState = MainState()

        reducer.assert(
            givenState = givenState,
            givenAction = MainAction.NavigateBack,

            expectedState = givenState
        )
    }

    @Test
    fun `it should remove last element from back stack and set it to current screen when back navigation performed`() {
        val givenState = MainState(
            screen = Screen.CREATE_TASK,
            backStack = listOf(Screen.TODO_LIST)
        )

        reducer.assert(
            givenState = givenState,
            givenAction = MainAction.NavigateBack,

            expectedState = MainState(
                screen = Screen.TODO_LIST
            )
        )
    }

    @Test
    fun `it should't change state when idle action performed`() {
        val givenState = MainState()

        reducer.assert(
            givenState = givenState,
            givenAction = MainAction.Idle,

            expectedState = givenState
        )
    }
}
