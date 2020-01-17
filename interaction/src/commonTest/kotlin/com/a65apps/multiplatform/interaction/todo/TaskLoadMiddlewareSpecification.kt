package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.mock.mockTaskRepository
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyAllTasks
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class TaskLoadMiddlewareSpecification : BaseMiddlewareSpecification<TodoAction, TodoState>() {

    lateinit var repository: TaskRepository

    override val defaultState: TodoState = TodoState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<TodoAction, TodoState> =
        TaskLoadMiddleware(repository, schedulers)

    @Test
    fun `it should load all tasks on success and emit Loaded action with loaded tasks`() {
        val tasks = listOf(
            Task("id", "title", "description", TaskStatus.PENDING)
        )
        repository.stubbing {
            stubAllTasks(singleOf(tasks))
        }
        val observer = bind()

        acceptAction(TodoAction.Load)

        observer.assertValue(TodoAction.Loaded(tasks))
        repository.verifyAllTasks()
    }

    @Test
    fun `it should emit Error action when repository failed to load all tasks`() {
        repository.stubbing {
            stubAllTasks(singleOfError(Throwable("error")))
        }
        val observer = bind()

        acceptAction(TodoAction.Load)

        observer.assertValue(TodoAction.Error("error"))
        repository.verifyAllTasks()
    }

    @Test
    fun `it should't accept any action except Load`() {
        val observer = bind()

        acceptAction(TodoAction.Idle)
        acceptAction(TodoAction.Archive)
        acceptAction(TodoAction.CreateTask)
        acceptAction(TodoAction.Switch("id"))
        acceptAction(TodoAction.Error("error"))
        acceptAction(TodoAction.Loaded(listOf()))
        acceptAction(TodoAction.Unarchive("id"))

        observer.assertNoValues()
    }
}
