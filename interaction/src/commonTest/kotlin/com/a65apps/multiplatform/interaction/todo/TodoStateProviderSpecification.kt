package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.base.isFrozen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TodoStateProviderSpecification {

    @Test
    fun `it should provide default state if restored state is empty`() {
        val provider = TodoStateProvider(null)

        val result = provider.get()

        assertEquals(
            expected = TodoState(),
            actual = result
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "provided state is not frozen"
        )
    }

    @Test
    fun `it should provide restored state if it is not empty`() {
        val given = TodoState(isLoading = true)
        val provider = TodoStateProvider(given)

        val result = provider.get()

        assertEquals(
            expected = given,
            actual = result
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "provided state is not frozen"
        )
    }
}
