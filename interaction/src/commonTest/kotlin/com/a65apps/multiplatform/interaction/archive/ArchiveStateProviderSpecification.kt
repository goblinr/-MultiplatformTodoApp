package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.interaction.base.isFrozen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArchiveStateProviderSpecification {

    @Test
    fun `it should provide default state if restored state is empty`() {
        val provider = ArchiveStateProvider(null)

        val result = provider.get()

        assertEquals(
            expected = ArchiveState(),
            actual = result
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "provided state is not frozen"
        )
    }

    @Test
    fun `it should provide restored state if it is not empty`() {
        val given = ArchiveState(isLoading = true)
        val provider = ArchiveStateProvider(given)

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
