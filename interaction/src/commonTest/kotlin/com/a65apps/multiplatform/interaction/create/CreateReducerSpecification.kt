package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.base.assert
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreateReducerSpecification {

    lateinit var reducer: CreateReducer

    @BeforeTest
    fun setUp() {
        reducer = CreateReducer()
    }

    @Test
    fun `it should't change state on Idle action`() {
        val givenState = CreateState()

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.Idle,

            expectedState = givenState
        )
    }

    @Test
    fun `it should change loading flag and clear error on Create action`() {
        val givenState = CreateState(error = "error")
        val expectedState = CreateState(isLoading = true, error = "")

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.Create,

            expectedState = expectedState
        )
    }

    @Test
    fun `it should change error field and clear loading flag on Error action`() {
        val givenState = CreateState(isLoading = true)
        val expectedState = CreateState(isLoading = false, error = "error")

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.Error("error"),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update title field on UpdateField action with TITLE type`() {
        val givenState = CreateState()
        val expectedState = CreateState(title = "title")

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.UpdateField(value = "title", type = FieldType.TITLE),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should update description field on UpdateField action with DESCRIPTION type`() {
        val givenState = CreateState()
        val expectedState = CreateState(description = "description")

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.UpdateField(
                value = "description",
                type = FieldType.DESCRIPTION
            ),

            expectedState = expectedState
        )
    }

    @Test
    fun `it should clear loading and error fields and set result field on CreateResult action`() {
        val task = Task("id", "title", "description", TaskStatus.PENDING)
        val givenState = CreateState(isLoading = true, error = "error")
        val expectedState = CreateState(result = task)

        reducer.assert(
            givenState = givenState,
            givenAction = CreateAction.CreateResult(task),

            expectedState = expectedState
        )
    }
}
