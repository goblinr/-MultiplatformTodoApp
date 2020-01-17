package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.base.isFrozen
import com.a65apps.multiplatform.interaction.navigation.Screen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainStateProviderSpecification {

    @Test
    fun `it should provide default state if restored state is empty`() {
        val provider = MainStateProvider(null)

        val result = provider.get()

        assertEquals(
            expected = MainState(),
            actual = result
        )
        assertTrue(
            actual = result.isFrozen(),
            message = "provided state is not frozen"
        )
    }

    @Test
    fun `it should provide restored state if it is not empty`() {
        val given = MainState(
            screen = Screen.CREATE_TASK
        )
        val provider = MainStateProvider(given)

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
