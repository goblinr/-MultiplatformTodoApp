package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.base.assert
import kotlin.test.BeforeTest
import kotlin.test.Test

class TodoReducerSpecification {

    private val id = "id"

    lateinit var reducer: TodoReducer

    @BeforeTest
    fun setUp() {
        reducer = TodoReducer()
    }

    @Test
    fun `it should't change state on Idle action`() {
        val givenState = TodoState()

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Idle,

            expectedState = givenState
        )
    }

    @Test
    fun `it should't change state on CreateTask action`() {
        val givenState = TodoState()

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.CreateTask,

            expectedState = givenState
        )
    }

    @Test
    fun `it should change loading flag on Switch action`() {
        val givenState = TodoState()
        val expectedState = TodoState(isLoading = true)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Switch(id),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should change loading flag on Archive action`() {
        val givenState = TodoState()
        val expectedState = TodoState(isLoading = true)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Archive,

            expectedState = expectedState
        )
    }

    @Test
    fun `it should change loading flag on Unarchive action`() {
        val givenState = TodoState()
        val expectedState = TodoState(isLoading = true)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Unarchive(id),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should change loading flag on Load action`() {
        val givenState = TodoState()
        val expectedState = TodoState(isLoading = true)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Load,

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear loading flag on Loaded action`() {
        val givenState = TodoState(isLoading = true)
        val expectedState = TodoState()

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Loaded(listOf()),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear error on Loaded action`() {
        val givenState = TodoState(error = "error")
        val expectedState = TodoState()

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Loaded(listOf()),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update task list on Loaded action`() {
        val tasks = listOf(
            Task(id, "title", "description", TaskStatus.PENDING)
        )
        val givenState = TodoState()
        val expectedState = TodoState(todoList = tasks)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Loaded(tasks),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update showArchive flag on Loaded action with DONE status`() {
        val tasks = listOf(
            Task(id, "title", "description", TaskStatus.DONE)
        )
        val givenState = TodoState()
        val expectedState = TodoState(todoList = tasks, showArchive = true)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Loaded(tasks),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update showArchive flag on Loaded action when DONE status not exists`() {
        val tasks = listOf(
            Task(id, "title", "description", TaskStatus.PENDING)
        )
        val givenState = TodoState(showArchive = true)
        val expectedState = TodoState(todoList = tasks)

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Loaded(tasks),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear loading flag end update error field on Error action`() {
        val givenState = TodoState(isLoading = true)
        val expectedState = TodoState(error = "error")

        reducer.assert(
            givenState = givenState,
            givenAction = TodoAction.Error("error"),

            expectedState = expectedState
        )
    }
}
