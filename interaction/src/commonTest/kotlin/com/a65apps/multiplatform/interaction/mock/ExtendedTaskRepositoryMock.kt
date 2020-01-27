package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals

class ExtendedTaskRepositoryMock(private val taskRepository: TaskRepository) :
    ExtendedTaskRepository {

    private val allArchivedTasksMock = object : Mock<MatcherKey0, Single<List<Task>>> {
        override var keys: List<MatcherKey0> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey0, Int>>(mapOf())
        override var stubs: Map<MatcherKey0, List<Single<List<Task>>>> = mapOf()
    }

    override fun archivedTasks(): Single<List<Task>> = stub(allArchivedTasksMock)

    override fun allTasks(): Single<List<Task>> = taskRepository.allTasks()

    override fun switchTask(id: String): Completable = taskRepository.switchTask(id)

    override fun archiveTasks(): Completable = taskRepository.archiveTasks()

    override fun unarchiveTask(id: String): Completable = taskRepository.unarchiveTask(id)

    override fun createTask(title: String, description: String): Single<Task> =
        taskRepository.createTask(title, description)

    override fun changed(): Observable<Unit> = taskRepository.changed()

    fun stubAllArchivedTasks(result: Single<List<Task>>) {
        stubMethod(allArchivedTasksMock, result)
    }

    fun verifyAllArchivedTasks(count: Int) {
        assertEquals(
            expected = count,
            actual = allArchivedTasksMock.invocations.value[MatcherKey0]
        )
    }
}

fun mockExtendedTaskRepository(): ExtendedTaskRepository =
    ExtendedTaskRepositoryMock(mockTaskRepository())

fun ExtendedTaskRepository.extendedStubbing(
    body: ExtendedTaskRepositoryMock.() -> Unit
) {
    kotlin.test.assertTrue(
        actual = this is ExtendedTaskRepositoryMock,
        message = "$this is not mocked"
    )

    body(this)
}

fun ExtendedTaskRepository.verifyAllArchivedTasks(count: Int = 1) {
    kotlin.test.assertTrue(
        actual = this is ExtendedTaskRepositoryMock,
        message = "$this is not mocked"
    )

    verifyAllArchivedTasks(count)
}
