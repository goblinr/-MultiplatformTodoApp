package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.mock.mockTaskRepository
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyArchiveTasks
import com.badoo.reaktive.completable.completableOfEmpty
import com.badoo.reaktive.completable.completableOfError
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class TaskArchiveMiddlewareSpecification : BaseMiddlewareSpecification<TodoAction, TodoState>() {

    lateinit var repository: TaskRepository

    override val defaultState: TodoState = TodoState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<TodoAction, TodoState> =
        TaskArchiveMiddleware(repository, schedulers)

    @Test
    fun `it should archive tasks on success and emit Idle action`() {
        repository.stubbing {
            stubArchiveTasks(completableOfEmpty())
        }
        val observer = bind()

        acceptAction(TodoAction.Archive)

        observer.assertValue(TodoAction.Idle)
        repository.verifyArchiveTasks()
    }

    @Test
    fun `it should emit Error action when repository failed to archive tasks`() {
        repository.stubbing {
            stubArchiveTasks(completableOfError(Throwable("error")))
        }
        val observer = bind()

        acceptAction(TodoAction.Archive)

        observer.assertValue(TodoAction.Error("error"))
        repository.verifyArchiveTasks()
    }

    @Test
    fun `it should't accept any action except Archive`() {
        val observer = bind()

        acceptAction(TodoAction.Idle)
        acceptAction(TodoAction.Load)
        acceptAction(TodoAction.CreateTask)
        acceptAction(TodoAction.Switch("id"))
        acceptAction(TodoAction.Error("error"))
        acceptAction(TodoAction.Loaded(listOf()))
        acceptAction(TodoAction.Unarchive("id"))

        observer.assertNoValues()
    }
}
