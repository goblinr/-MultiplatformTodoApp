package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.mock.extendedStubbing
import com.a65apps.multiplatform.interaction.mock.mockExtendedTaskRepository
import com.a65apps.multiplatform.interaction.mock.verifyAllArchivedTasks
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.singleOfError
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class TaskLoadArchiveMiddlewareSpecification :
    BaseMiddlewareSpecification<ArchiveAction, ArchiveState>() {

    lateinit var repository: ExtendedTaskRepository

    override val defaultState: ArchiveState = ArchiveState()

    override fun initMocks() {
        repository = mockExtendedTaskRepository()
    }

    override fun createMiddleware(): Middleware<ArchiveAction, ArchiveState> =
        TaskLoadArchiveMiddleware(repository, schedulers)

    @Test
    fun `it should load all tasks with status ARCHIVED on success and emit Loaded action with loaded tasks`() {
        val tasks = listOf(
            Task("id", "title", "description", TaskStatus.ARCHIVED)
        )
        repository.extendedStubbing {
            stubAllArchivedTasks(singleOf(tasks))
        }
        val observer = bind()

        acceptAction(ArchiveAction.Load)

        observer.assertValue(ArchiveAction.Loaded(tasks))
        repository.verifyAllArchivedTasks()
    }

    @Test
    fun `it should emit Error action when repository failed to load all tasks`() {
        repository.extendedStubbing {
            stubAllArchivedTasks(singleOfError(Throwable("error")))
        }
        val observer = bind()

        acceptAction(ArchiveAction.Load)

        observer.assertValue(ArchiveAction.Error("error"))
        repository.verifyAllArchivedTasks()
    }

    @Test
    fun `it should't accept any action except Load`() {
        val observer = bind()

        acceptAction(ArchiveAction.Idle)
        acceptAction(ArchiveAction.UnarchiveTask("id"))
        acceptAction(ArchiveAction.Error("error"))
        acceptAction(ArchiveAction.Loaded(listOf()))

        observer.assertNoValues()
    }
}
