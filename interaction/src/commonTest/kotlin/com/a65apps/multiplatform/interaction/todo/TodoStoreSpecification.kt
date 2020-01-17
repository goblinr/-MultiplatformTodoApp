package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.StateSubjectProvider
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockReducer
import com.a65apps.multiplatform.interaction.mock.mockStateProvider
import com.a65apps.multiplatform.interaction.mock.mockStateSubjectProvider
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.test.observable.TestObservableObserver
import com.badoo.reaktive.test.observable.assertValues
import kotlin.test.BeforeTest
import kotlin.test.Test

class TodoStoreSpecification {

    private val defaultState = TodoState()

    lateinit var reducer: Reducer<TodoState, TodoAction>
    lateinit var initialState: StateProvider<TodoState>
    lateinit var stateSubjectProvider: StateSubjectProvider<TodoState>
    lateinit var store: TodoStore
    lateinit var subject: Subject<TodoState>
    lateinit var observer: TestObservableObserver<TodoState>

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
    fun `it should accept Load action at start`() {
        // given
        val expectedState = TodoState(
            isLoading = true
        )
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(TodoAction.Load), expectedState)
        }

        // when
        initStore()

        // then
        observer.assertValues(listOf(defaultState, expectedState))
    }

    private fun initStore() {
        store = TodoStore(
            reducer = reducer,
            middleware = setOf(),
            initialState = initialState,
            stateSubjectProvider = stateSubjectProvider
        )
    }
}
