package com.a65apps.multiplatform.interaction

import com.a65apps.multiplatform.interaction.mock.mockState
import com.a65apps.multiplatform.interaction.mock.mockStateProvider
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.badoo.reaktive.test.observable.TestObservableObserver
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class DefaultStateSubjectProviderSpecification {

    @Test
    fun `it should get default state from initial state provider`() {
        // given
        val initialState = mockStateProvider<State>()
        val state = mockState()
        initialState.stubbing {
            stub(state)
        }
        val observer = TestObservableObserver<State>()
        val provider = DefaultStateSubjectProvider<State>()

        // when
        provider.provide(initialState).subscribe(observer)

        // then
        observer.assertValue(state)
    }
}
