package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.mock.eq
import com.a65apps.multiplatform.interaction.mock.mockTaskRepository
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyCreateTask
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class TaskCreateMiddlewareSpecification : BaseMiddlewareSpecification<CreateAction, CreateState>() {

    lateinit var repository: TaskRepository

    override val defaultState = CreateState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<CreateAction, CreateState> =
        TaskCreateMiddleware(repository, schedulers)

    @Test
    fun `it should create task if current state is correct and response is successful`() {
        val result = Task("id", "title", "description", TaskStatus.PENDING)
        repository.stubbing {
            stubCreateTask(eq("title"), eq("description"), singleOf(result))
        }
        prepareState(
            CreateState(title = "title", description = "description")
        )
        val observer = bind()

        acceptAction(CreateAction.Create)

        observer.assertValue(CreateAction.CreateResult(result))
        repository.verifyCreateTask(eq("title"), eq("description"))
    }

    @Test
    fun `it should emit error when title is empty`() {
        prepareState(CreateState())

        val observer = bind()

        acceptAction(CreateAction.Create)

        observer.assertValue(CreateAction.Error("Title should't be empty"))
    }

    @Test
    fun `it should emit error when description is empty`() {
        prepareState(CreateState(title = "title"))

        val observer = bind()

        acceptAction(CreateAction.Create)

        observer.assertValue(CreateAction.Error("Description should't be empty"))
    }

    @Test
    fun `it should emit error when request is failed`() {
        repository.stubbing {
            stubCreateTask(eq("title"), eq("description"),
                singleOfError(Throwable("error")))
        }
        prepareState(
            CreateState(title = "title", description = "description")
        )
        val observer = bind()

        acceptAction(CreateAction.Create)

        observer.assertValue(CreateAction.Error("error"))
        repository.verifyCreateTask(eq("title"), eq("description"))
    }
}
