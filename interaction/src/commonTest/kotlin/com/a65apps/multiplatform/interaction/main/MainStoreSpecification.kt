package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockReducer
import com.a65apps.multiplatform.interaction.mock.mockStateProvider
import com.a65apps.multiplatform.interaction.mock.mockStateSubjectProvider
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.test.observable.TestObservableObserver
import com.badoo.reaktive.test.observable.assertValue
import com.badoo.reaktive.test.observable.assertValues
import kotlin.test.BeforeTest
import kotlin.test.Test

class MainStoreSpecification {

    private val defaultState = MainState()
    private val action = MainAction.NavigateReplace(
        screen = Screen.TODO_LIST
    )

    lateinit var reducer: Reducer<MainState, MainAction>
    lateinit var initialState: StateProvider<MainState>
    lateinit var stateSubjectProvider: StateSubjectProvider<MainState>
    lateinit var store: MainStore
    lateinit var subject: Subject<MainState>
    lateinit var observer: TestObservableObserver<MainState>

    @BeforeTest
    fun setUp() {
        reducer = mockReducer()
        initialState = mockStateProvider()
        stateSubjectProvider = mockStateSubjectProvider()
        observer = TestObservableObserver()
        subject = BehaviorSubject(defaultState)
        subject.subscribe(observer)
        stateSubjectProvider.stubbing {
            stubProvide(eq(initialState), subject)
        }
    }

    @Test
    fun `it should accept navigation replace action with TODO_LIST screen when restored state is empty`() {
        // given
        val expectedState = MainState(
            screen = Screen.TODO_LIST
        )
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(action), expectedState)
        }

        // when
        initStore()

        // then
        observer.assertValues(listOf(defaultState, expectedState))
    }

    @Test
    fun `it should not accept navigation replace action with TODO_LIST screen when restored state is not empty`() {
        // given
        val restoredState = MainState(
            screen = Screen.TODO_LIST
        )

        // when
        initStore(restoredState)

        // then
        observer.assertValue(restoredState)
    }

    private fun initStore(restoredState: MainState? = null) {
        store = MainStore(
            reducer = reducer,
            middleware = setOf(),
            initialState = initialState,
            stateSubjectProvider = stateSubjectProvider,
            restoredState = restoredState
        )
    }
}
