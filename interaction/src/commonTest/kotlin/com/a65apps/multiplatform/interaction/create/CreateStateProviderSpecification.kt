package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.interaction.base.isFrozen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateStateProviderSpecification {

    @Test
    fun `it should provide default state if restored state is empty`() {
        val provider = CreateStateProvider(null)

        val result = provider.get()

        assertEquals(
            expected = CreateState(),
            actual = result
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "provided state is not frozen"
        )
    }

    @Test
    fun `it should provide restored state if it is not empty`() {
        val given = CreateState(
            isLoading = true
        )
        val provider = CreateStateProvider(given)

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
