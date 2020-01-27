package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.base.assert
import kotlin.test.BeforeTest
import kotlin.test.Test

class ArchiveReducerSpecification {

    private val id = "id"

    lateinit var reducer: ArchiveReducer

    @BeforeTest
    fun setUp() {
        reducer = ArchiveReducer()
    }

    @Test
    fun `it should't change state on Idle action`() {
        val givenState = ArchiveState()

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Idle,

            expectedState = givenState
        )
    }

    @Test
    fun `it should change loading flag on Load action`() {
        val givenState = ArchiveState()
        val expectedState = ArchiveState(isLoading = true)

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Load,

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear loading flag on Loaded action`() {
        val givenState = ArchiveState(isLoading = true)
        val expectedState = ArchiveState()

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Loaded(listOf()),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear error on Loaded action`() {
        val givenState = ArchiveState(error = "error")
        val expectedState = ArchiveState()

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Loaded(listOf()),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update task list on Loaded action`() {
        val tasks = listOf(
            Task(id, "title", "description", TaskStatus.ARCHIVED)
        )
        val givenState = ArchiveState()
        val expectedState = ArchiveState(archiveTodoList = tasks)

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Loaded(tasks),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear loading flag end update error field on Error action`() {
        val givenState = ArchiveState(isLoading = true)
        val expectedState = ArchiveState(error = "error")

        reducer.assert(
            givenState = givenState,
            givenAction = ArchiveAction.Error("error"),

            expectedState = expectedState
        )
    }
}
