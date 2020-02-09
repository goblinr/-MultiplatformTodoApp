package com.a65apps.multiplatform.interaction.archive

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

class TaskUnarchiveMiddlewareSpecification : BaseMiddlewareSpecification<ArchiveAction, ArchiveState>() {

    lateinit var repository: TaskRepository

    override val defaultState: ArchiveState = ArchiveState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<ArchiveAction, ArchiveState> =
        TaskUnarchiveMiddleware(repository, schedulers)

    @Test
    fun `it should unarchive task by id on success and emit Load action`() {
        val id = "id"
        repository.stubbing {
            stubUnarchiveTask(eq(id), completableOfEmpty())
        }
        val observer = bind()

        acceptAction(ArchiveAction.UnarchiveTask(id))

        observer.assertValue(ArchiveAction.Load)
        repository.verifyUnarchiveTask(eq(id))
    }

    @Test
    fun `it should emit Error action when repository failed to switch task status`() {
        val id = "id"
        repository.stubbing {
            stubUnarchiveTask(eq(id), completableOfError(Throwable("error")))
        }
        val observer = bind()

        acceptAction(ArchiveAction.UnarchiveTask(id))

        observer.assertValue(ArchiveAction.Error("error"))
        repository.verifyUnarchiveTask(eq(id))
    }

    @Test
    fun `it should't accept any action except Unarchive`() {
        val observer = bind()

        acceptAction(ArchiveAction.Idle)
        acceptAction(ArchiveAction.Load)
        acceptAction(ArchiveAction.Loaded(listOf()))
        acceptAction(ArchiveAction.Error("error"))

        observer.assertNoValues()
    }
}
