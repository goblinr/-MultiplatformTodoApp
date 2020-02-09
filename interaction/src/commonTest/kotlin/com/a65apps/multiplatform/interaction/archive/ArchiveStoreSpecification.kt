package com.a65apps.multiplatform.interaction.archive

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

class ArchiveStoreSpecification {

    private val defaultState = ArchiveState()

    lateinit var reducer: Reducer<ArchiveState, ArchiveAction>
    lateinit var initialState: StateProvider<ArchiveState>
    lateinit var stateSubjectProvider: StateSubjectProvider<ArchiveState>
    lateinit var store: ArchiveStore
    lateinit var subject: Subject<ArchiveState>
    lateinit var observer: TestObservableObserver<ArchiveState>

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
        val expectedState = ArchiveState(
            isLoading = true
        )
        reducer.stubbing {
            stubReduce(eq(defaultState), eq(ArchiveAction.Load), expectedState)
        }

        // when
        initStore()

        // then
        observer.assertValues(listOf(defaultState, expectedState))
    }

    private fun initStore() {
        store = ArchiveStore(
            reducer = reducer,
            middleware = setOf(),
            initialState = initialState,
            stateSubjectProvider = stateSubjectProvider
        )
    }
}
