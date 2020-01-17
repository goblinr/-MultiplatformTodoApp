package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockTaskRepository
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyUnarchiveTask
import com.badoo.reaktive.completable.completableOfEmpty
import com.badoo.reaktive.completable.completableOfError
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class TaskUnarchiveMiddlewareSpecification : BaseMiddlewareSpecification<TodoAction, TodoState>() {

    lateinit var repository: TaskRepository

    override val defaultState: TodoState = TodoState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<TodoAction, TodoState> =
        TaskUnarchiveMiddleware(repository, schedulers)

    @Test
    fun `it should unarchive task status with selected id`() {
        val id = "id"
        repository.stubbing {
            stubUnarchiveTask(eq(id), completableOfEmpty())
        }
        val observer = bind()

        acceptAction(TodoAction.Unarchive(id))

        observer.assertValue(TodoAction.Idle)
        repository.verifyUnarchiveTask(eq(id))
    }

    @Test
    fun `it should emit Error action when repository failed to switch task status`() {
        val id = "id"
        repository.stubbing {
            stubUnarchiveTask(eq(id), completableOfError(Throwable("error")))
        }
        val observer = bind()

        acceptAction(TodoAction.Unarchive(id))

        observer.assertValue(TodoAction.Error("error"))
        repository.verifyUnarchiveTask(eq(id))
    }

    @Test
    fun `it should't accept any action except Unarchive`() {
        val observer = bind()

        acceptAction(TodoAction.Idle)
        acceptAction(TodoAction.Archive)
        acceptAction(TodoAction.CreateTask)
        acceptAction(TodoAction.Load)
        acceptAction(TodoAction.Error("error"))
        acceptAction(TodoAction.Loaded(listOf()))
        acceptAction(TodoAction.Switch("id"))

        observer.assertNoValues()
    }
}
