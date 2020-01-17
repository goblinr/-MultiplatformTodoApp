package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertEquals

class TaskRepositoryMock : TaskRepository {

    private val allTasksMock = object : Mock<MatcherKey0, Single<List<Task>>> {
        override var keys: List<MatcherKey0> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey0, Int>>(mapOf())
        override var stubs: Map<MatcherKey0, List<Single<List<Task>>>> = mapOf()
    }

    private val switchTaskMock = object : Mock<MatcherKey1<String>, Completable> {
        override var keys: List<MatcherKey1<String>> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey1<String>, Int>>(mapOf())
        override var stubs: Map<MatcherKey1<String>, List<Completable>> = mapOf()
    }

    private val archiveTasksMock = object : Mock<MatcherKey0, Completable> {
        override var keys: List<MatcherKey0> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey0, Int>>(mapOf())
        override var stubs: Map<MatcherKey0, List<Completable>> = mapOf()
    }

    private val unarchiveTaskMock = object : Mock<MatcherKey1<String>, Completable> {
        override var keys: List<MatcherKey1<String>> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey1<String>, Int>>(mapOf())
        override var stubs: Map<MatcherKey1<String>, List<Completable>> = mapOf()
    }

    private val createTaskMock = object : Mock<MatcherKey2<String, String>, Single<Task>> {
        override var keys: List<MatcherKey2<String, String>> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey2<String, String>, Int>>(mapOf())
        override var stubs: Map<MatcherKey2<String, String>, List<Single<Task>>> = mapOf()
    }

    private val changedMock = object : Mock<MatcherKey0, Observable<Unit>> {
        override var keys: List<MatcherKey0> = listOf()
        override val invocations = AtomicReference<Map<MatcherKey0, Int>>(mapOf())
        override var stubs: Map<MatcherKey0, List<Observable<Unit>>> = mapOf()
    }

    override fun allTasks(): Single<List<Task>> = stub(allTasksMock)

    override fun switchTask(id: String): Completable = stub(
        mock = switchTaskMock,
        first = id
    )

    override fun archiveTasks(): Completable = stub(archiveTasksMock)

    override fun unarchiveTask(id: String): Completable = stub(
        mock = unarchiveTaskMock,
        first = id
    )

    override fun createTask(title: String, description: String): Single<Task> = stub(
        mock = createTaskMock,
        first = title,
        second = description
    )

    override fun changed(): Observable<Unit> = stub(changedMock)

    fun stubAllTasks(result: Single<List<Task>>) {
        stubMethod(allTasksMock, result)
    }

    fun verifyAllTasks(count: Int) {
        assertEquals(
            expected = count,
            actual = allTasksMock.invocations.value[MatcherKey0]
        )
    }

    fun stubSwitchTask(id: Matcher<String>, result: Completable) {
        stubMethod(switchTaskMock, id, result)
    }

    fun verifySwitchTask(id: Matcher<String>, count: Int) {
        assertEquals(
            expected = count,
            actual = switchTaskMock.invocations.value[MatcherKey1(id)]
        )
    }

    fun stubArchiveTasks(result: Completable) {
        stubMethod(archiveTasksMock, result)
    }

    fun verifyArchiveTasks(count: Int) {
        assertEquals(
            expected = count,
            actual = archiveTasksMock.invocations.value[MatcherKey0]
        )
    }

    fun stubUnarchiveTask(id: Matcher<String>, result: Completable) {
        stubMethod(unarchiveTaskMock, id, result)
    }

    fun verifyUnarchiveTask(id: Matcher<String>, count: Int) {
        assertEquals(
            expected = count,
            actual = unarchiveTaskMock.invocations.value[MatcherKey1(id)]
        )
    }

    fun stubCreateTask(title: Matcher<String>, description: Matcher<String>, result: Single<Task>) {
        stubMethod(createTaskMock, title, description, result)
    }

    fun verifyCreateTask(title: Matcher<String>, description: Matcher<String>, count: Int) {
        assertEquals(
            expected = count,
            actual = createTaskMock.invocations.value[MatcherKey2(title, description)]
        )
    }

    fun stubChanged(result: Observable<Unit>) {
        stubMethod(changedMock, result)
    }

    fun verifyChanged(count: Int) {
        assertEquals(
            expected = count,
            actual = changedMock.invocations.value[MatcherKey0]
        )
    }
}

fun mockTaskRepository(): TaskRepository = TaskRepositoryMock()

fun TaskRepository.stubbing(
    body: TaskRepositoryMock.() -> Unit
) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    body(this)
}

fun TaskRepository.verifyAllTasks(count: Int = 1) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifyAllTasks(count)
}

fun TaskRepository.verifySwitchTask(id: Matcher<String>, count: Int = 1) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifySwitchTask(id, count)
}

fun TaskRepository.verifyArchiveTasks(count: Int = 1) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifyArchiveTasks(count)
}

fun TaskRepository.verifyUnarchiveTask(id: Matcher<String>, count: Int = 1) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifyUnarchiveTask(id, count)
}

fun TaskRepository.verifyCreateTask(
    title: Matcher<String>,
    description: Matcher<String>,
    count: Int = 1
) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifyCreateTask(title, description, count)
}

fun TaskRepository.verifyChanged(count: Int = 1) {
    kotlin.test.assertTrue(actual = this is TaskRepositoryMock, message = "$this is not mocked")

    verifyChanged(count)
}
