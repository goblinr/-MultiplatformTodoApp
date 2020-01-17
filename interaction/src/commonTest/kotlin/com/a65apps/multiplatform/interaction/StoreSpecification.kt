package com.a65apps.multiplatform.interaction

import com.a65apps.multiplatform.interaction.mock.any
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockAction
import com.a65apps.multiplatform.interaction.mock.mockMiddleware
import com.a65apps.multiplatform.interaction.mock.mockReducer
import com.a65apps.multiplatform.interaction.mock.mockState
import com.a65apps.multiplatform.interaction.mock.mockStateProvider
import com.a65apps.multiplatform.interaction.mock.mockStateSubjectProvider
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyBind
import com.a65apps.multiplatform.interaction.mock.verifyProvide
import com.a65apps.multiplatform.interaction.mock.verifyReduce
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.test.observable.TestObservableObserver
import com.badoo.reaktive.test.observable.assertValues
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreSpecification {

    lateinit var reducer: Reducer<State, Action>
    lateinit var middleware: Middleware<Action, State>
    lateinit var initialState: StateProvider<State>
    lateinit var stateSubjectProvider: StateSubjectProvider<State>
    lateinit var store: Store<State, Action>

    @BeforeTest
    fun setUp() {
        reducer = mockReducer()
        middleware = mockMiddleware()
        initialState = mockStateProvider()
        stateSubjectProvider = mockStateSubjectProvider()
    }

    @Test
    fun `it should call provide on StateSubjectProvider with initial StateProvider`() {
        // given
        val result = BehaviorSubject(mockState())
        stateSubjectProvider.stubbing {
            stubProvide(
                initialState = eq(initialState),
                result = result
            )
        }

        // when
        initStore(setOf())

        // then
        assertEquals(
            expected = result,
            actual = store.states()
        )
        stateSubjectProvider.verifyProvide(eq(initialState))
    }

    @Test
    fun `it should reduce action to new state`() {
        // given
        val defaultState = mockState()
        val expectedState = mockState()
        val action = mockAction()
        val observer = TestObservableObserver<State>()
        val stateSubject = BehaviorSubject(defaultState)
        stateSubject.subscribe(observer)

        stateSubjectProvider.stubbing {
            stubProvide(
                initialState = eq(initialState),
                result = stateSubject
            )
        }
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(action), expectedState)
        }

        // when
        initStore(setOf())
        store.acceptAction(action)

        // then
        observer.assertValues(listOf(defaultState, expectedState))
        reducer.verifyReduce(eq(defaultState), eq(action))
    }

    @Test
    fun `it should call bind on middleware and connect middleware actions to action bus`() {
        // given
        val defaultState = mockState()
        val expectedState = mockState()
        val action = mockAction()
        val observer = TestObservableObserver<State>()
        val stateSubject = BehaviorSubject(defaultState)
        val middlewareActions = PublishSubject<Action>()
        stateSubject.subscribe(observer)

        stateSubjectProvider.stubbing {
            stubProvide(
                initialState = eq(initialState),
                result = stateSubject
            )
        }
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(action), expectedState)
        }
        middleware.stubbing {
            stubBind(any(), any(), middlewareActions)
        }

        // when
        initStore(setOf(middleware))
        middlewareActions.onNext(action)

        // then
        observer.assertValues(listOf(defaultState, expectedState))
        middleware.verifyBind(any(), any())
    }

    @Test
    fun `it should stop emitting states after dispose called`() {
        val defaultState = mockState()
        val expectedState = mockState()
        val action = mockAction()
        val observer = TestObservableObserver<State>()
        val stateSubject = BehaviorSubject(defaultState)
        stateSubject.subscribe(observer)

        stateSubjectProvider.stubbing {
            stubProvide(
                initialState = eq(initialState),
                result = stateSubject
            )
        }
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(action), expectedState)
        }
        initStore(setOf())
        store.acceptAction(action)

        // when
        store.dispose()
        store.acceptAction(action)

        // then
        observer.assertValues(listOf(defaultState, expectedState))
    }

    private fun initStore(middleware: Set<Middleware<Action, State>>) {
        store = Store(
            reducer = reducer,
            middleware = middleware,
            initialState = initialState,
            stateSubjectProvider = stateSubjectProvider
        )
    }
}
